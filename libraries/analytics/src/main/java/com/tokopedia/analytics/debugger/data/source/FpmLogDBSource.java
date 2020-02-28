package com.tokopedia.analytics.debugger.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.analytics.database.FpmLogDB;
import com.tokopedia.analytics.database.TkpdAnalyticsDatabase;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;
import com.tokopedia.analytics.performance.PerformanceLogModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/16/18.
 */
public class FpmLogDBSource {

    private FpmLogDao fpmLogDao;

    @Inject
    public FpmLogDBSource(Context context) {
        fpmLogDao = TkpdAnalyticsDatabase.getInstance(context).fpmLogDao();
    }

    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(subscriber -> {
            fpmLogDao.deleteAll();
            subscriber.onNext(true);
        });
    }

    public Observable<Boolean> insertAll(PerformanceLogModel data) {
        return Observable.just(data).map(analyticsLogData -> {
            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            FpmLogDB fpmLogDB = new FpmLogDB();
            fpmLogDB.setTraceName(data.getTraceName());
            fpmLogDB.setDuration(data.getEndTime() - data.getStartTime());
            try {
                fpmLogDB.setAttributes(URLDecoder.decode(gson.toJson(data.getAttributes()), "UTF-8"));
                fpmLogDB.setMetrics(URLDecoder.decode(gson.toJson(data.getMetrics()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                fpmLogDB.setAttributes("UnsupportedEncodingException");
                fpmLogDB.setMetrics("UnsupportedEncodingException");
                e.printStackTrace();
            }
            fpmLogDB.setTimestamp(new Date().getTime());
            fpmLogDao.insertAll(fpmLogDB);
            return true;
        });
    }

    public Observable<List<FpmLogDB>> getData(HashMap<String, Object> params) {
        return Observable.just(params).map(params1 -> {
            int page;

            if (params1.containsKey(AnalyticsDebuggerConst.PAGE))
                page = (int) params1.get(AnalyticsDebuggerConst.PAGE);
            else page = 0;

            String search = "%%";

            if (params1.containsKey(AnalyticsDebuggerConst.KEYWORD)) {
                String keyword = (String) params1.get(AnalyticsDebuggerConst.KEYWORD);
                search = "%" + keyword + "%";
            }

            int offset = 20 * page;
            return fpmLogDao.getData(search, offset);
        });
    }

    public Observable<List<FpmLogDB>> getAllData() {
        return Observable.just(1).map(number -> fpmLogDao.getAllData());
    }
}
