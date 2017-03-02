package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appsflyer.SingleInstallBroadcastReceiver;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.tagmanager.TagManager;
import com.localytics.android.ReferralReceiver;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.InstallReceiver;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ricoharisin on 7/8/15.
 * modified by alvarisi
 */
public class GTMDataLayer {

    static void pushGeneral(Context context, Map<String, Object> values) {
        Log.i("GAv4", "UA-9801603-15: Send General");

        GTMBody gtmBody = new GTMBody();
        gtmBody.context = context;
        gtmBody.values = values;

        Observable.just(gtmBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GTMBody, Boolean>() {
                    @Override
                    public Boolean call(GTMBody data) {
                        TagManager.getInstance(data.context).getDataLayer().push(data.values);
                        return true;
                    }
                })
                .unsubscribeOn(Schedulers.newThread())
                .subscribe();
    }

    static void pushEvent(Context context, String eventName, Map<String, Object> values) {
        Log.i("GAv4", "UA-9801603-15: Send Event");

        GTMBody gtmBody = new GTMBody();
        gtmBody.context = context;
        gtmBody.values = values;
        gtmBody.eventName = eventName;

        Observable.just(gtmBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GTMBody, Boolean>() {
                    @Override
                    public Boolean call(GTMBody data) {
                        TagManager.getInstance(data.context).getDataLayer().pushEvent(data.eventName, data.values);
                        return true;
                    }
                })
                .unsubscribeOn(Schedulers.newThread())
                .subscribe();

    }

    private static class GTMBody {
        Context context;
        Map<String, Object> values;
        String eventName;
    }
}
