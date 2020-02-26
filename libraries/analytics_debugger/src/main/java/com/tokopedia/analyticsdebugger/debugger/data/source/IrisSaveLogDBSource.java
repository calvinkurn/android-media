package com.tokopedia.analyticsdebugger.debugger.data.source;

import android.content.Context;

import com.tokopedia.analyticsdebugger.database.IrisSaveLogDB;
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase;
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

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
        return Observable.fromCallable(() ->{
            irisLogSaveDao.insertAll(irisSaveLogDB);
            return true;
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
