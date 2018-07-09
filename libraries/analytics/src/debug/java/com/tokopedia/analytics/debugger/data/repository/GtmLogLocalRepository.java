package com.tokopedia.analytics.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.database.GtmLogDB;
import com.tokopedia.analytics.debugger.data.mapper.GtmLogMapper;
import com.tokopedia.analytics.debugger.data.source.GtmLogDBSource;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogLocalRepository implements GtmLogRepository {
    private GtmLogDBSource gtmLogDBSource;
    private GtmLogMapper gtmLogMapper;

    @Inject
    GtmLogLocalRepository(GtmLogDBSource gtmLogDBSource,
                          GtmLogMapper gtmLogMapper) {
        this.gtmLogDBSource = gtmLogDBSource;
        this.gtmLogMapper = gtmLogMapper;
    }

    @Override
    public Observable<Boolean> insert(AnalyticsLogData data) {
        return gtmLogDBSource.insertAll(data);
    }

    @Override
    public Observable<Boolean> removeAll() {
        return gtmLogDBSource.deleteAll();
    }

    @Override
    public Observable<List<Visitable>> get(RequestParams parameters) {
        return gtmLogDBSource.getData(parameters.getParameters())
                .flatMapIterable(new Func1<List<GtmLogDB>, Iterable<GtmLogDB>>() {
                    @Override
                    public Iterable<GtmLogDB> call(List<GtmLogDB> gtmLogDBS) {
                        return gtmLogDBS;
                    }
                }).flatMap(gtmLogMapper).toList();
    }
}
