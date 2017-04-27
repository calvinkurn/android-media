package com.tokopedia.core.gcm;

import android.os.Bundle;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * @author by alvarisi on 12/20/16.
 */

public class NotificationAnalyticsReceiver implements INotificationAnalyticsReceiver {
    public NotificationAnalyticsReceiver() {
    }

    @Override
    public void onNotificationReceived(Observable<Bundle> bundle) {
        bundle.map(new Func1<Bundle, Boolean>() {
            @Override
            public Boolean call(Bundle data) {
                sendNotificationCodeReceived(data.getString(ARG_NOTIFICATION_CODE));
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Actions.empty(), new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

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
