package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel
import com.tokopedia.usecase.RequestParams

import rx.Observable

interface FpmLogRepository {

    val allData: Observable<List<Visitable<*>>>
    fun insert(data: PerformanceLogModel): Observable<Boolean>

    fun removeAll(): Observable<Boolean>

    operator fun get(parameters: RequestParams): Observable<List<Visitable<*>>>
}
