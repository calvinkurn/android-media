package com.tokopedia.analytics.debugger.data.source;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.abstraction.base.data.source.database.DataDBSource;
import com.tokopedia.analytics.database.GtmLogDB;
import com.tokopedia.analytics.database.GtmLogDB_Table;
import com.tokopedia.analytics.debugger.AnalyticsDebuggerConst;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogDBSource implements DataDBSource<AnalyticsLogData, List<GtmLogDB>> {
    @Inject
    public GtmLogDBSource() {

    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(GtmLogDB.class).hasData());
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(GtmLogDB.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insertAll(AnalyticsLogData data) {
        return Observable.just(data).map(new Func1<AnalyticsLogData, Boolean>() {
            @Override
            public Boolean call(AnalyticsLogData analyticsLogData) {
                GtmLogDB gtmLogDB = new GtmLogDB();
                gtmLogDB.setName(analyticsLogData.getName());
                gtmLogDB.setCategory(analyticsLogData.getCategory());
                gtmLogDB.setData(analyticsLogData.getData());
                gtmLogDB.setTimestamp(new Date().getTime());

                gtmLogDB.save();
                return true;
            }
        });
    }

    @Override
    public Observable<List<GtmLogDB>> getData(HashMap<String, Object> params) {
        return Observable.just(params).map(new Func1<HashMap<String, Object>, List<GtmLogDB>>() {
            @Override
            public List<GtmLogDB> call(HashMap<String, Object> params) {
                int page;

                if (params.containsKey(AnalyticsDebuggerConst.PAGE))
                    page = (int) params.get(AnalyticsDebuggerConst.PAGE);
                else page = 0;

                ConditionGroup conditions = ConditionGroup.clause();
                if (params.containsKey(AnalyticsDebuggerConst.KEYWORD)) {
                    String keyword = (String) params.get(AnalyticsDebuggerConst.KEYWORD);
                    conditions.or(GtmLogDB_Table.name.like("%" + keyword + "%"))
                            .or(GtmLogDB_Table.data.like("%" + keyword + "%"))
                            .or(GtmLogDB_Table.category.like("%" + keyword + "%"));
                }

                return new Select()
                        .from(GtmLogDB.class)
                        .where(conditions)
                        .offset(20 * page)
                        .limit(20)
                        .orderBy(OrderBy.fromProperty(GtmLogDB_Table.timestamp).descending())
                        .queryList();
            }
        });
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        return Observable.just(params).map(new Func1<HashMap<String, Object>, Integer>() {
            @Override
            public Integer call(HashMap<String, Object> stringObjectHashMap) {
                return (int) new Select(Method.count()).from(GtmLogDB.class).count();
            }
        });
    }
}
