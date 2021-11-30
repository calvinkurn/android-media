package com.tokopedia.core.gcm;

import android.os.Bundle;

import com.tokopedia.core.analytics.AppEventTracking;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

}
