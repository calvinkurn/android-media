package com.tokopedia.analytics.debugger.data.source;

import android.content.Context;

import com.tokopedia.analytics.database.GtmErrorLogDB;
import com.tokopedia.analytics.database.IrisSaveLogDB;
import com.tokopedia.analytics.database.TkpdAnalyticsDatabase;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class IrisSaveLogDBSource {

    private IrisLogSaveDao irisLogSaveDao;

    @Inject
    public IrisSaveLogDBSource(Context context) {
        irisLogSaveDao = TkpdAnalyticsDatabase.getInstance(context).irisLogSaveDao();
    }

    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(subscriber -> {
            irisLogSaveDao.deleteAll();
            subscriber.onNext(true);
        });
    }

    public Observable<Boolean> insertAll(IrisSaveLogDB irisSaveLogDB) {
        return Observable.just(irisSaveLogDB).map(new Func1<IrisSaveLogDB, Boolean>() {
            @Override
            public Boolean call(IrisSaveLogDB saveLogDB) {
                irisLogSaveDao.insertAll(saveLogDB);
                return true;
            }
        });
    }

    public Observable<List<IrisSaveLogDB>> getData(HashMap<String, Object> params) {
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
            return irisLogSaveDao.getData(search, offset);
        });
    }

}
