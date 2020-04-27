package com.tokopedia.analyticsdebugger.debugger.data.source;

import android.content.Context;

import com.tokopedia.analyticsdebugger.database.GtmErrorLogDB;
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase;
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmErrorLogDBSource {

    private GtmErrorLogDao gtmErrorLogDao;

    @Inject
    public GtmErrorLogDBSource(Context context) {
        gtmErrorLogDao = TkpdAnalyticsDatabase.getInstance(context).gtmErrorLogDao();
    }

    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(subscriber -> {
            gtmErrorLogDao.deleteAll();
            subscriber.onNext(true);
        });
    }

    public Observable<Boolean> insertAll(GtmErrorLogDB gtmErrorLogDB) {
        return Observable.fromCallable(() ->{
            gtmErrorLogDao.insertAll(gtmErrorLogDB);
            return true;
        });
    }

    public Observable<List<GtmErrorLogDB>> getData(HashMap<String, Object> params) {
        return Observable.fromCallable(() ->{
            int page;

            if (params.containsKey(AnalyticsDebuggerConst.PAGE))
                page = (int) params.get(AnalyticsDebuggerConst.PAGE);
            else page = 0;

            String search = "%%";

            if (params.containsKey(AnalyticsDebuggerConst.KEYWORD)) {
                String keyword = (String) params.get(AnalyticsDebuggerConst.KEYWORD);
                search = "%" + keyword + "%";
            }

            int offset = 100 * page;
            return gtmErrorLogDao.getData(search, offset);
        });
    }

}
