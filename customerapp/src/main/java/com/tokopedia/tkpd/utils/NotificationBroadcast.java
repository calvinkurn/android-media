package com.tokopedia.tkpd.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(CustomPushListener.DELETE_NOTIFY)) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(intent.getExtras().getInt(CustomPushListener.EXTRA_DELETE_NOTIFICATION_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
