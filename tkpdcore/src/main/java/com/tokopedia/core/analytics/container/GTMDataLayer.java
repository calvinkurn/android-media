package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.tagmanager.TagManager;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
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

        log(gtmBody);

        Observable.just(gtmBody)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<GTMBody, Boolean>() {
                    @Override
                    public Boolean call(GTMBody data) {
                        TagManager.getInstance(data.context).getDataLayer().push(data.values);
                        return true;
                    }
                })
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    static void pushEvent(Context context, String eventName, Map<String, Object> values) {
        Log.i("GAv4", "UA-9801603-15: Send Event");

        GTMBody gtmBody = new GTMBody();
        gtmBody.context = context;
        gtmBody.values = values;
        gtmBody.eventName = eventName;

        log(gtmBody);

        Observable.just(gtmBody)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<GTMBody, Boolean>() {
                    @Override
                    public Boolean call(GTMBody data) {
                        TagManager.getInstance(data.context).getDataLayer().pushEvent(data.eventName, data.values);
                        return true;
                    }
                })
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });

    }

    private static void log(GTMBody gtmBody) {
        try {
            AnalyticsLogData data = new AnalyticsLogData();
            data.setCategory((String) gtmBody.values.get("eventCategory"));
            data.setName(gtmBody.eventName == null ? (String) gtmBody.values.get("event") : gtmBody.eventName);
            data.setData(gtmBody.values.toString());
            GtmLogger.getInstance().save(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class GTMBody {
        Context context;
        Map<String, Object> values;
        String eventName;
    }
}
