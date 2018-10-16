package com.tokopedia.kelontongapp.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by meta on 16/10/18.
 */
public class KelontongFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        String message = remoteMessage.getNotification().getBody();
        Log.d("Meyta", "From: " + from);
        Log.d("Meyta", "Notification Message Body: " + message);
    }
}
