package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmErrorLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsDebuggerActivity;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsGtmErrorDebuggerActivity;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analyticsdebugger.database.GtmErrorLogDB;

import java.net.URLDecoder;
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

    private Context context;
    private GtmLogDBSource dbSource;
    private GtmErrorLogDBSource dbErrorSource;
    private LocalCacheHandler cache;

    private GtmLogger(Context context) {
        this.context = context;
        this.dbSource = new GtmLogDBSource(context);
        this.dbErrorSource = new GtmErrorLogDBSource(context);
        this.cache = new LocalCacheHandler(context, ANALYTICS_DEBUGGER);
    }

    public static AnalyticsLogger getInstance(Context context) {
        if (instance == null) {
            if(GlobalConfig.isAllowDebuggingTools()) {
                instance = new GtmLogger(context);
            } else {
                instance = emptyInstance();
            }
        }

        return instance;
    }

    @Override
    public void save(String name, Map<String, Object> mapData) {
        try {
            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

            AnalyticsLogData data = new AnalyticsLogData();
            data.setCategory((String) mapData.get("eventCategory"));
            data.setName(name);
            data.setData(URLDecoder.decode(gson.toJson(mapData)
                    .replaceAll("%(?![0-9a-fA-F]{2})", "%25")
                    .replaceAll("\\+", "%2B")
                    , "UTF-8"));

            if (!TextUtils.isEmpty(data.getName()) && !data.getName().equals("null")) {
                dbSource.insertAll(data).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
            }

            if(cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false)) {
                NotificationHelper.show(context, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveError(String errorData) {
        GtmErrorLogDB gtmErrorLogDB = new GtmErrorLogDB();
        gtmErrorLogDB.setData(errorData);
        gtmErrorLogDB.setTimestamp(System.currentTimeMillis());
        if(cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false)) {
            AnalyticsLogData data = new AnalyticsLogData();
            data.setCategory("");
            data.setName("error GTM v5");
            data.setData(errorData);
            NotificationHelper.show(context, data);
        }
        dbErrorSource.insertAll(gtmErrorLogDB).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    @Override
    public void wipe() {
        dbSource.deleteAll().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    @Override
    public void openActivity() {
        context.startActivity(AnalyticsDebuggerActivity.newInstance(context));
    }

    @Override
    public void openErrorActivity() {
        context.startActivity(AnalyticsGtmErrorDebuggerActivity.newInstance(context));
    }

    @Override
    public void enableNotification(boolean isEnabled) {
        cache.putBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, isEnabled);
        cache.applyEditor();
    }

    @Override
    public boolean isNotificationEnabled() {
        return cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false);
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
            public void save(String name, Map<String, Object> data) {

            }

            @Override
            public void saveError(String errorData) {

            }

            @Override
            public void wipe() {

            }

            @Override
            public void openActivity() {

            }

            @Override
            public void openErrorActivity() {

            }

            @Override
            public void enableNotification(boolean status) {

            }

            @Override
            public boolean isNotificationEnabled() {
                return false;
            }
        };
    }
}
