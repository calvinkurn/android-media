package com.tokopedia.analytics.debugger;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analytics.debugger.data.source.GtmLogDBSource;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;
import com.tokopedia.analytics.debugger.ui.activity.AnalyticsDebuggerActivity;

import java.util.Map;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogger implements AnalyticsLogger {
    private static final String ANALYTICS_DEBUGGER = "ANALYTICS_DEBUGGER";
    private static final String IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled";

    private static AnalyticsLogger instance;

    private GtmLogDBSource dbSource;

    private GtmLogger() {
        dbSource = new GtmLogDBSource();
    }

    public static AnalyticsLogger getInstance() {
        if (instance == null) {
            if(GlobalConfig.isAllowDebuggingTools()) {
                instance = new GtmLogger();
            } else {
                instance = emptyInstance();
            }
        }

        return instance;
    }

    @Override
    public void save(Context context, String name, Map<String, Object> mapData) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            AnalyticsLogData data = new AnalyticsLogData();
            data.setCategory((String) mapData.get("eventCategory"));
            data.setName(name);
            data.setData(gson.toJson(mapData));

            if (!TextUtils.isEmpty(data.getName()) && !data.getName().equals("null")) {
                dbSource.insertAll(data).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
            }

            LocalCacheHandler cache = new LocalCacheHandler(context, ANALYTICS_DEBUGGER);
            if(cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false)) {
                NotificationHelper.show(context, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wipe() {
        dbSource.deleteAll().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    @Override
    public void openActivity(Context context) {
        context.startActivity(AnalyticsDebuggerActivity.newInstance(context));
    }

    @Override
    public void enableNotification(Context context, boolean isEnabled) {
        LocalCacheHandler cache = new LocalCacheHandler(context, ANALYTICS_DEBUGGER);
        cache.putBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, isEnabled);
        cache.applyEditor();
    }

    @Override
    public boolean isNotificationEnabled(Context context) {
        LocalCacheHandler analyticsCache = new LocalCacheHandler(context, ANALYTICS_DEBUGGER);
        return analyticsCache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false);
    }

    private Subscriber<? super Boolean> defaultSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                // no-op
            }
        };
    }

    private static AnalyticsLogger emptyInstance() {
        return new AnalyticsLogger() {
            @Override
            public void save(Context context, String name, Map<String, Object> data) {

            }

            @Override
            public void wipe() {

            }

            @Override
            public void openActivity(Context context) {

            }

            @Override
            public void enableNotification(Context context, boolean status) {

            }

            @Override
            public boolean isNotificationEnabled(Context context) {
                return false;
            }
        };
    }
}
