package com.tokopedia.analytics.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;
import com.tokopedia.analytics.debugger.ui.model.FpmDebuggerViewModel;
import com.tokopedia.analytics.performance.PerformanceLogModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

public interface FpmLogRepository {
    Observable<Boolean> insert(PerformanceLogModel data);

    Observable<Boolean> removeAll();

    Observable<List<Visitable>> get(RequestParams parameters);

    Observable<List<Visitable>> getAllData();
}
