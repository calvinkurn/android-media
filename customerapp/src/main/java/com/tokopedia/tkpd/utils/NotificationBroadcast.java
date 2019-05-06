package com.tokopedia.tkpd.utils;

import android.app.NotificationManager;
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
            if (CustomPushListener.DELETE_NOTIFY.equals(action)) {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(intent.getExtras().getInt(CustomPushListener.EXTRA_DELETE_NOTIFICATION_ID));
            } else if (CustomPushListener.EXTRA_ACTION_PERSISTENT_CLICK.equals(action)) {
                handlePersistentClick(context, intent);
            }
        } catch (Exception e) {
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

    public void postMoengageEvent(String eventName, Map<String, Object> attributeMap) {
        TrackApp.getInstance().getMoEngage().sendTrackEvent(eventName, attributeMap);
    }
}
