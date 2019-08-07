package com.tokopedia.analytics.debugger.data.source;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.database.DataDBSource;
import com.tokopedia.analytics.database.GtmLogDB;
import com.tokopedia.analytics.database.TkpdAnalyticsDatabase;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogDBSource implements DataDBSource<AnalyticsLogData, List<GtmLogDB>> {

    private GtmLogDao gtmLogDao;

    @Inject
    public GtmLogDBSource(Context context) {
        gtmLogDao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao();
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(subscriber -> {
            List<GtmLogDB> list = gtmLogDao.getData();
            subscriber.onNext(list != null && !list.isEmpty());
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(subscriber -> {
            gtmLogDao.deleteAll();
            subscriber.onNext(true);
        });
    }

    @Override
    public Observable<Boolean> insertAll(AnalyticsLogData data) {
        return Observable.just(data).map(analyticsLogData -> {
            GtmLogDB gtmLogDB = new GtmLogDB();
            gtmLogDB.setName(analyticsLogData.getName());
            gtmLogDB.setCategory(analyticsLogData.getCategory());
            gtmLogDB.setData(analyticsLogData.getData());
            gtmLogDB.setTimestamp(new Date().getTime());
            gtmLogDao.insertAll(gtmLogDB);
            return true;
        });
    }

    @Override
    public Observable<List<GtmLogDB>> getData(HashMap<String, Object> params) {
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
            return gtmLogDao.getData(search, offset);
        });
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        return Observable.just(params).map(stringObjectHashMap -> gtmLogDao.getCount());
    }
}
