package com.tokopedia.tkpd.utils;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.track.TrackApp;

import java.util.HashMap;
import java.util.Map;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (null == action)
                return;
            int notificationId = intent.getExtras().getInt(CustomPushListener.EXTRA_NOTIFICATION_ID);
            System.out.println("NotificationBroadcast " + action + " id:" + notificationId);
            switch (action) {
                case CustomPushListener.ACTION_DELETE_NOTIFY:
                    cancelNotification(context, notificationId);
                    break;
                case CustomPushListener.ACTION_GRID_CLICK:
                    handleGridClick(context, intent, notificationId);
                    break;
                case CustomPushListener.ACTION_PERSISTENT_CLICK:
                    handlePersistentClick(context, intent);
                    break;
            }
        } catch (Exception e) {

            System.out.println("NotificationBroadcast deleting:" +e.getMessage());
            e.printStackTrace();
        }
    }

    private void handlePersistentClick(Context context, Intent intent) {
        try {
            String iconName = intent.getStringExtra(CustomPushListener.EXTRA_PERSISTENT_ICON_NAME);
            String iconUrl = intent.getStringExtra(CustomPushListener.EXTRA_PERSISTENT_ICON_URL);
            String deepLink = intent.getStringExtra(CustomPushListener.EXTRA_PERSISTENT_DEEPLINK);
            String campaignId = intent.getStringExtra(CustomPushListener.EXTRA_CAMPAIGN_ID);

            Intent appLinkIntent = RouteManager.getIntent(context.getApplicationContext(), deepLink);
            appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appLinkIntent.putExtras(intent.getExtras());
            context.startActivity(appLinkIntent);

            context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

            Map<String, Object> attributeMap = new HashMap<>();
            attributeMap.put(CustomPushListener.EXTRA_PERSISTENT_DEEPLINK, deepLink);
            attributeMap.put(CustomPushListener.EXTRA_PERSISTENT_ICON_NAME, iconName);
            attributeMap.put(CustomPushListener.EXTRA_PERSISTENT_ICON_URL, iconUrl);
            attributeMap.put(CustomPushListener.EXTRA_CAMPAIGN_ID, campaignId);
            postMoengageEvent(CustomPushListener.EVENT_PERSISTENT_CLICK_NAME, attributeMap);

        } catch (Exception e) {
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

        System.out.println("NotificationBroadcast deleting:" + notificationId);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationId);
    }

    public void postMoengageEvent(String eventName, Map<String, Object> attributeMap) {
        TrackApp.getInstance().getMoEngage().sendTrackEvent(eventName, attributeMap);
    }
}
