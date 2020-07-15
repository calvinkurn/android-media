package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData
import com.tokopedia.usecase.RequestParams

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
interface GtmLogRepository {
    fun insert(data: AnalyticsLogData): Observable<Boolean>

    fun removeAll(): Observable<Boolean>

    operator fun get(parameters: RequestParams): Observable<List<Visitable<*>>>
}
