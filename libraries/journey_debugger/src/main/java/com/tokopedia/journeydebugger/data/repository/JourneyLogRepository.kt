package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel
import com.tokopedia.usecase.RequestParams

import rx.Observable

interface JourneyLogRepository {
    fun insert(data: ApplinkLogModel): Observable<Boolean>

    fun removeAll(): Observable<Boolean>

    operator fun get(parameters: RequestParams): Observable<List<Visitable<*>>>
}
