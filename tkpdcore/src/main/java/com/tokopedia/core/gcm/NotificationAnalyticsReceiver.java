package com.tokopedia.core.gcm;

import android.os.Bundle;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * @author by alvarisi on 12/20/16.
 */

public class NotificationAnalyticsReceiver implements INotificationAnalyticsReceiver {
    public NotificationAnalyticsReceiver() {
    }

    @Override
    public void onNotificationReceived(Bundle data) {
        sendNotificationCodeReceived(data.getString(ARG_NOTIFICATION_CODE));
    }

    @Override
    public Bundle buildAnalyticNotificationData(Bundle data) {
        if (data != null && data.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)) {
            HashMap<String, Object> maps = new HashMap<>();
            try {
                JSONObject llObject = new JSONObject(data.getString(AppEventTracking.LOCA.NOTIFICATION_BUNDLE));
                Iterator<?> keys = llObject.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    maps.put(key, llObject.getString(key));
                }
                data.putSerializable(AppEventTracking.LOCA.NOTIFICATION_BUNDLE, maps);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendLocalyticsPushReceived(data);
        }
        return data;

    }

    private void sendNotificationCodeReceived(String code) {
        String eventName = "event : gcm notification";
        Map<String, String> params = new HashMap<>();
        params.put("Notification code", code);
        TrackingUtils.eventLocaNotification(eventName, params);
    }

    private void sendLocalyticsPushReceived(Bundle data) {
        TrackingUtils.eventLocaNotificationReceived(data);
    }
}
