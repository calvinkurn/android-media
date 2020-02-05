package com.tokopedia.analytics.debugger;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.analytics.database.IrisSaveLogDB;
import com.tokopedia.analytics.database.IrisSendLogDB;
import com.tokopedia.analytics.debugger.data.source.IrisSaveLogDBSource;
import com.tokopedia.analytics.debugger.data.source.IrisSendLogDBSource;
import com.tokopedia.analytics.debugger.ui.activity.AnalyticsIrisSaveDebuggerActivity;
import com.tokopedia.analytics.debugger.ui.activity.AnalyticsIrisSendDebuggerActivity;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class IrisLogger implements IrisLoggerInterface {

    private static IrisLoggerInterface instance;

    private IrisSaveLogDBSource irisSaveLogDBSource;
    private IrisSendLogDBSource irisSendLogDBSource;
    private Context context;

    private IrisLogger(Context context) {
        this.context = context;
        this.irisSendLogDBSource = new IrisSendLogDBSource(context);
        this.irisSaveLogDBSource = new IrisSaveLogDBSource(context);
    }

    public static IrisLoggerInterface getInstance(Context context) {
        if (instance == null) {
            if(GlobalConfig.isAllowDebuggingTools()) {
                instance = new IrisLogger(context);
            } else {
                instance = emptyInstance();
            }
        }

        return instance;
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

    private static IrisLoggerInterface emptyInstance() {
        return new IrisLoggerInterface() {
            @Override
            public void putSendIrisEvent(String data) {

            }

            @Override
            public void putSaveIrisEvent(String data) {

            }

            @Override
            public void openSaveActivity() {

            }

            @Override
            public void openSendActivity() {

            }
        };
    }

    @Override
    public void putSaveIrisEvent(String data) {
        IrisSaveLogDB irisSaveLogDB = new IrisSaveLogDB();
        irisSaveLogDB.setData(data);
        irisSaveLogDB.setTimestamp(System.currentTimeMillis());
        irisSaveLogDBSource.insertAll(irisSaveLogDB)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    @Override
    public void putSendIrisEvent(String data) {
        IrisSendLogDB irisSendLogDB = new IrisSendLogDB();
        irisSendLogDB.setData(data);
        irisSendLogDB.setTimestamp(System.currentTimeMillis());
        irisSendLogDBSource.insertAll(irisSendLogDB)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    @Override
    public void openSaveActivity() {
        context.startActivity(AnalyticsIrisSaveDebuggerActivity.newInstance(context));
    }

    @Override
    public void openSendActivity() {
        context.startActivity(AnalyticsIrisSendDebuggerActivity.newInstance(context));
    }
}
