package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.analyticsdebugger.database.IrisSaveLogDB;
import com.tokopedia.analyticsdebugger.database.IrisSendLogDB;
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisSaveLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisSendLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsIrisSaveDebuggerActivity;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsIrisSendDebuggerActivity;
import com.tokopedia.config.GlobalConfig;

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
            if (GlobalConfig.isAllowDebuggingTools()) {
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
            public void putSendIrisEvent(String data, int rowCount) {

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
        irisSaveLogDB.setData(prettify(data));
        irisSaveLogDB.setTimestamp(System.currentTimeMillis());
        irisSaveLogDBSource.insertAll(irisSaveLogDB)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    @Override
    public void putSendIrisEvent(String data, int rowCount) {
        IrisSendLogDB irisSendLogDB = new IrisSendLogDB();
        irisSendLogDB.setData(rowCount + " - " + prettify(data));
        irisSendLogDB.setTimestamp(System.currentTimeMillis());
        irisSendLogDBSource.insertAll(irisSendLogDB)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
    }

    private String prettify(String jsonString) {
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
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
