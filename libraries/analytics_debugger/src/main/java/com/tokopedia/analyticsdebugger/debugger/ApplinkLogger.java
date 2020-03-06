package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analyticsdebugger.debugger.data.source.ApplinkLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.ApplinkDebuggerActivity;
import com.tokopedia.config.GlobalConfig;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class ApplinkLogger implements ApplinkLoggerInterface {
    private static final String APPLINK_DEBUGGER = "APPLINK_DEBUGGER";
    private static final String IS_APPLINK_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled";

    private static ApplinkLoggerInterface instance;

    private Context context;
    private ApplinkLogDBSource dbSource;
    private LocalCacheHandler cache;

    private ApplinkLogger(Context context) {
        this.context = context;
        this.dbSource = new ApplinkLogDBSource(context);
        this.cache = new LocalCacheHandler(context, APPLINK_DEBUGGER);
    }

    public static void init(Context context) {
        if(GlobalConfig.isAllowDebuggingTools()) {
            instance = new ApplinkLogger(context);
        } else {
            instance = emptyInstance();
        }
    }

    public static ApplinkLoggerInterface getInstance() {
        return instance;
    }

    @Override
    public void save(String applink,
                     String trace) {
        try {
            ApplinkLogModel applinkLogModel = new ApplinkLogModel();
            applinkLogModel.setApplink(applink);
            applinkLogModel.setTraces(trace);

            dbSource.insertAll(applinkLogModel).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());

            if(cache.getBoolean(IS_APPLINK_DEBUGGER_NOTIF_ENABLED, false)) {
                NotificationHelper.show(context, applinkLogModel);
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
        context.startActivity(ApplinkDebuggerActivity.newInstance(context));
    }

    @Override
    public void enableNotification(boolean isEnabled) {
        cache.putBoolean(IS_APPLINK_DEBUGGER_NOTIF_ENABLED, isEnabled);
        cache.applyEditor();
    }

    @Override
    public boolean isNotificationEnabled() {
        return cache.getBoolean(IS_APPLINK_DEBUGGER_NOTIF_ENABLED, false);
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

    private static ApplinkLoggerInterface emptyInstance() {
        return new ApplinkLoggerInterface() {
            @Override
            public void save(String applink, String trace) {

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
