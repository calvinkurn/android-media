package com.tokopedia.analyticsdebugger.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

public interface ApplinkLogRepository {
    Observable<Boolean> insert(ApplinkLogModel data);

    Observable<Boolean> removeAll();

    Observable<List<Visitable>> get(RequestParams parameters);
}
