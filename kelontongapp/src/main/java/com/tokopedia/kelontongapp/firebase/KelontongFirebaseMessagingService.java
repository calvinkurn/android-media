package com.tokopedia.kelontongapp.firebase;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.kelontongapp.notification.NotificationFactory;

import java.util.Map;

/**
 * Created by meta on 16/10/18.
 */
public class KelontongFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);

        Log.d("FCM ", data.toString());

        NotificationFactory.showNotification(this, data);
    }

    protected Bundle convertMap(RemoteMessage message){
        Map<String, String> map = message.getData();
        Bundle bundle = new Bundle(map != null ? map.size() : 0);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }
}
