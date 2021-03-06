package com.example.androidserversocket;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends Activity {

	TextView info, infoip, msg;
	String message = "";
	ServerSocket serverSocket;
	BufferedReader input;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		info = (TextView) findViewById(R.id.info);
		infoip = (TextView) findViewById(R.id.infoip);
		msg = (TextView) findViewById(R.id.msg);
		
		infoip.setText(getIpAddress());

		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class SocketServerThread extends Thread {

		static final int SocketServerPORT = 8080;
		int count = 0;

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(SocketServerPORT);
				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						info.setText("I'm waiting here: "
								+ serverSocket.getLocalPort());
					}
				});

				while (true) {
					Socket socket = serverSocket.accept();

					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					final String read = input.readLine();
					Log.v("python_read", read);



					count++;
					message += "#" + count + " from " + socket.getInetAddress()
							+ ":" + socket.getPort() + "\n";

					MainActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							msg.setText(read);




							if ( read.contains("one")){
								Log.v("python_read", "case 1");
								Intent intent = new  Intent(Intent.ACTION_VIEW);
								intent.setPackage("com.google.android.youtube");
								intent.setData(Uri.parse("https://www.youtube.com/watch?v=0H5mA7qoM1g"));
								startActivity(intent);

							}else if(read.contains("two")){

								Intent intent = new  Intent(Intent.ACTION_VIEW);
								intent.setPackage("com.google.android.youtube");
								intent.setData(Uri.parse("https://www.youtube.com/watch?v=lNasPo7QP6s"));
								startActivity(intent);

								Log.v("python_read", "case 2");
							}else if(read.contains("play")){
								Intent intent = new  Intent(Intent.ACTION_VIEW);
								intent.setPackage("com.google.android.youtube");
								String link = read.substring(5);
								intent.setData(Uri.parse(link));
								startActivity(intent);


							} else{

								Intent intent = new  Intent(Intent.ACTION_VIEW);
								intent.setPackage("com.google.android.youtube");
								intent.setData(Uri.parse("https://www.youtube.com/watch?v=6v2L2UGZJAM"));
								startActivity(intent);

								Log.v("python_read", "three");

							}






						}
					});
/**
					SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
							socket, count);
					socketServerReplyThread.run();

**/



				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	/**

	private class SocketServerReplyThread extends Thread {

		private Socket hostThreadSocket;
		int cnt;

		SocketServerReplyThread(Socket socket, int c) {
			hostThreadSocket = socket;
			cnt = c;
		}

		@Override
		public void run() {
			OutputStream outputStream;
			String msgReply = "Hello from Android, you are #" + cnt;

			try {
				outputStream = hostThreadSocket.getOutputStream();
	            PrintStream printStream = new PrintStream(outputStream);
	            printStream.print(msgReply);
	            printStream.close();

				//message += "replayed: " + msgReply + "\n";

				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						msg.setText(message);
						Intent intent = new  Intent(Intent.ACTION_VIEW);

						intent.setPackage("com.google.android.youtube");
						intent.setData(Uri.parse("https://www.youtube.com/watch?v=0H5mA7qoM1g"));

						startActivity(intent);

					}
				});

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message += "Something wrong! " + e.toString() + "\n";
			}

			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					//msg.setText(message);
				}
			});
		}

	}


	**/



	private String getIpAddress() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += "SiteLocalAddress: " 
								+ inetAddress.getHostAddress() + "\n";
					}
					
				}

			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}

		return ip;
	}
}
