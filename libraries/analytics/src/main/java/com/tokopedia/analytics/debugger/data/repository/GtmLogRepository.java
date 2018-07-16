package com.tokopedia.analytics.debugger.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.domain.model.AnalyticsLogData;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * @author okasurya on 5/16/18.
 */
public interface GtmLogRepository {
    Observable<Boolean> insert(AnalyticsLogData data);

    Observable<Boolean> removeAll();

    Observable<List<Visitable>> get(RequestParams parameters);
}
