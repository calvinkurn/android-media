package com.tokopedia.analyticsdebugger.debugger.data.source;

import android.content.Context;

import com.tokopedia.analyticsdebugger.database.IrisSendLogDB;
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase;
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

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
        return Observable.fromCallable(() ->{
            irisLogSendDao.insertAll(irisSendLogDB);
            return true;
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
