package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analyticsdebugger.debugger.data.source.FpmLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.FpmDebuggerActivity;
import com.tokopedia.config.GlobalConfig;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class FpmLogger implements PerformanceLogger {
    private static final String FPM_DEBUGGER = "FPM_DEBUGGER";
    private static final String IS_FPM_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled";

    private static PerformanceLogger instance;

    private Context context;
    private FpmLogDBSource dbSource;
    private LocalCacheHandler cache;

    private FpmLogger(Context context) {
        this.context = context;
        this.dbSource = new FpmLogDBSource(context);
        this.cache = new LocalCacheHandler(context, FPM_DEBUGGER);
    }

    public static PerformanceLogger getInstance(Context context) {
        if (instance == null) {
            if(GlobalConfig.isAllowDebuggingTools()) {
                instance = new FpmLogger(context);
            } else {
                instance = emptyInstance();
            }
        }

        return instance;
    }

    @Override
    public void save(PerformanceLogModel performanceLogModel) {
        try {

            dbSource.insertAll(performanceLogModel).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());

            if(cache.getBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, false)) {
                NotificationHelper.show(context, performanceLogModel);
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
    public void openActivity() {
        context.startActivity(FpmDebuggerActivity.newInstance(context));
    }

    @Override
    public void enableNotification(boolean isEnabled) {
        cache.putBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, isEnabled);
        cache.applyEditor();
    }

    @Override
    public boolean isNotificationEnabled() {
        return cache.getBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, false);
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

    private static PerformanceLogger emptyInstance() {
        return new PerformanceLogger() {
            @Override
            public void save(PerformanceLogModel model) {

            }

            @Override
            public void wipe() {

            }

            @Override
            public void openActivity() {

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
