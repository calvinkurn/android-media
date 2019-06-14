package com.tokopedia.tkpd.utils;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.applink.RouteManager;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (null == action)
                return;
            int notificationId = intent.getExtras().getInt(CustomPushListener.EXTRA_NOTIFICATION_ID);
            switch (action) {
                case CustomPushListener.DELETE_NOTIFY:
                    cancelNotification(context, notificationId);
                    break;
                case CustomPushListener.ACTION_GRID_CLICK:
                    handleGridClick(context, intent, notificationId);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGridClick(Context context, Intent intent, int notificationId) {
        String deepLink = intent.getExtras().getString(CustomPushListener.EXTRA_DEEP_LINK);
        try {
            Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), deepLink);
            appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appLinkIntent.putExtras(intent.getExtras());
            context.startActivity(appLinkIntent);
        } catch (ActivityNotFoundException e) {
        }
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        cancelNotification(context, notificationId);
    }

    public void cancelNotification(Context context, int notificationId) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationId);
    }
}
