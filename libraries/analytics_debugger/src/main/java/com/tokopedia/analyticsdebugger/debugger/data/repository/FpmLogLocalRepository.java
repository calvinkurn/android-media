package com.tokopedia.analyticsdebugger.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.database.FpmLogDB;
import com.tokopedia.analyticsdebugger.debugger.data.mapper.FpmLogMapper;
import com.tokopedia.analyticsdebugger.debugger.data.source.FpmLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class FpmLogLocalRepository implements FpmLogRepository {
    private FpmLogDBSource fpmLogDBSource;
    private FpmLogMapper fpmLogMapper;

    @Inject
    FpmLogLocalRepository(FpmLogDBSource fpmLogDBSource,
                          FpmLogMapper fpmLogMapper) {
        this.fpmLogDBSource = fpmLogDBSource;
        this.fpmLogMapper = fpmLogMapper;
    }

    @Override
    public Observable<Boolean> insert(PerformanceLogModel data) {
        return fpmLogDBSource.insertAll(data);
    }

    @Override
    public Observable<Boolean> removeAll() {
        return fpmLogDBSource.deleteAll();
    }

    @Override
    public Observable<List<Visitable>> get(RequestParams parameters) {
        return fpmLogDBSource.getData(parameters.getParameters())
                .flatMapIterable(new Func1<List<FpmLogDB>, Iterable<FpmLogDB>>() {
                    @Override
                    public Iterable<FpmLogDB> call(List<FpmLogDB> fpmLogDBS) {
                        return fpmLogDBS;
                    }
                }).flatMap(fpmLogMapper).toList();
    }

    @Override
    public Observable<List<Visitable>> getAllData() {
        return fpmLogDBSource.getAllData()
                .flatMapIterable(new Func1<List<FpmLogDB>, Iterable<FpmLogDB>>() {
                    @Override
                    public Iterable<FpmLogDB> call(List<FpmLogDB> fpmLogDBS) {
                        return fpmLogDBS;
                    }
                }).flatMap(fpmLogMapper).toList();
    }
}
