package com.tokopedia.analytics.debugger.data.source;

import android.content.Context;

import com.tokopedia.analytics.database.IrisSaveLogDB;
import com.tokopedia.analytics.database.IrisSendLogDB;
import com.tokopedia.analytics.database.TkpdAnalyticsDatabase;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class IrisSendLogDBSource {

    private IrisLogSendDao irisLogSendDao;

    @Inject
    public IrisSendLogDBSource(Context context) {
        irisLogSendDao = TkpdAnalyticsDatabase.getInstance(context).irisLogSendDao();
    }

    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(subscriber -> {
            irisLogSendDao.deleteAll();
            subscriber.onNext(true);
        });
    }

    public Observable<Boolean> insertAll(IrisSendLogDB irisSendLogDB) {
        return Observable.just(irisSendLogDB).map(new Func1<IrisSendLogDB, Boolean>() {
            @Override
            public Boolean call(IrisSendLogDB irisSendLogDB) {
                irisLogSendDao.insertAll(irisSendLogDB);
                return true;
            }
        });
    }

    public Observable<List<IrisSendLogDB>> getData(HashMap<String, Object> params) {
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
            return irisLogSendDao.getData(search, offset);
        });
    }

}
