package com.tokopedia.analytics.debugger;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.analytics.debugger.data.source.GtmLogDBSource;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;

import java.util.Map;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogger implements AnalyticsLogger {
    private static AnalyticsLogger instance;

    private GtmLogDBSource dbSource;

    public static AnalyticsLogger getInstance() {
        if(instance == null) {
            if(GlobalConfig.isAllowDebuggingTools()) {
                instance = new GtmLogger();
            } else {
                instance = new NoOpLogger();
            }
        }

        return instance;
    }

    private GtmLogger() {
        dbSource = new GtmLogDBSource();
    }

    @Override
    public void save(String name, Map<String, Object> mapData) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            AnalyticsLogData data = new AnalyticsLogData();
            data.setCategory((String) mapData.get("eventCategory"));
            data.setName(name);
            data.setData(gson.toJson(mapData));

            if(!TextUtils.isEmpty(data.getName()) && !data.getName().equals("null")) {
                dbSource.insertAll(data).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wipe() {
        dbSource.deleteAll().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber());
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
}
