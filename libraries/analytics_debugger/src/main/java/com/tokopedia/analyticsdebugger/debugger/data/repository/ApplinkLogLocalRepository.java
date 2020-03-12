package com.tokopedia.analyticsdebugger.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.database.ApplinkLogDB;
import com.tokopedia.analyticsdebugger.debugger.data.mapper.ApplinkLogMapper;
import com.tokopedia.analyticsdebugger.debugger.data.source.ApplinkLogDBSource;
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class ApplinkLogLocalRepository implements ApplinkLogRepository {
    private ApplinkLogDBSource applinkLogDBSource;
    private ApplinkLogMapper applinkLogMapper;

    @Inject
    ApplinkLogLocalRepository(ApplinkLogDBSource applinkLogDBSource,
                              ApplinkLogMapper applinkLogMapper) {
        this.applinkLogDBSource = applinkLogDBSource;
        this.applinkLogMapper = applinkLogMapper;
    }

    @Override
    public Observable<Boolean> insert(ApplinkLogModel data) {
        return applinkLogDBSource.insertAll(data);
    }

    @Override
    public Observable<Boolean> removeAll() {
        return applinkLogDBSource.deleteAll();
    }

    @Override
    public Observable<List<Visitable>> get(RequestParams parameters) {
        return applinkLogDBSource.getData(parameters.getParameters())
                .flatMapIterable(new Func1<List<ApplinkLogDB>, Iterable<ApplinkLogDB>>() {
                    @Override
                    public Iterable<ApplinkLogDB> call(List<ApplinkLogDB> ApplinkLogDBS) {
                        return ApplinkLogDBS;
                    }
                }).flatMap(applinkLogMapper).toList();
    }
}
