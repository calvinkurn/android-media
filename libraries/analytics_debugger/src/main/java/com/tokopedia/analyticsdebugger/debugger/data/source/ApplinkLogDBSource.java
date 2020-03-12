package com.tokopedia.analyticsdebugger.debugger.data.source;

import android.content.Context;

import com.tokopedia.analyticsdebugger.database.ApplinkLogDB;
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase;
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst;
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class ApplinkLogDBSource {

    private ApplinkLogDao applinkLogDao;

    @Inject
    public ApplinkLogDBSource(Context context) {
        applinkLogDao = TkpdAnalyticsDatabase.getInstance(context).applinkLogDao();
    }

    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(subscriber -> {
            applinkLogDao.deleteAll();
            subscriber.onNext(true);
        });
    }

    public Observable<Boolean> insertAll(ApplinkLogModel data) {
        return Observable.just(data).map(analyticsLogData -> {
            ApplinkLogDB applinkLogDB = new ApplinkLogDB();
            applinkLogDB.setApplink(data.getApplink());
            applinkLogDB.setTraces(data.getTraces());
            applinkLogDB.setTimestamp(new Date().getTime());

            applinkLogDao.insertAll(applinkLogDB);
            return true;
        });
    }

    public Observable<List<ApplinkLogDB>> getData(HashMap<String, Object> params) {
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
            return applinkLogDao.getData(search, offset);
        });
    }
}
