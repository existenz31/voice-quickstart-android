package com.twilio.voice.quickstart.notify;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpClientListen implements Runnable {
    PacketListener listener;

    @SuppressLint("LongLogTag")
    @Override
    public void run() {
        boolean run = true;
        DatagramSocket udpSocket = null;
        try {
            udpSocket = new DatagramSocket(3030);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        while (run) {
            try {
                byte[] message = new byte[8000];
                DatagramPacket packet = new DatagramPacket(message,message.length);
                Log.i("UDP client: ", "about to wait to receive");
                udpSocket.receive(packet);
                String text = new String(message, 0, packet.getLength());
                if (listener != null) listener.onPacketReceived(text);
            }catch (IOException e) {
                Log.e("UDP client has IOException", "error: ", e);
                run = false;
            }
        }
    }

    public interface PacketListener {
        public void onPacketReceived(String text);
    }

    // Assign the listener implementing events interface that will receive the events
    public void setPacketListener(PacketListener listener) {
        this.listener = listener;
    }
}
