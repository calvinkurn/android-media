package com.tokopedia.journeydebugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.journeydebugger.domain.model.JourneyLogModel
import com.tokopedia.usecase.RequestParams

import rx.Observable

interface JourneyLogRepository {
    fun insert(data: JourneyLogModel): Observable<Boolean>

    fun removeAll(): Observable<Boolean>

    operator fun get(parameters: RequestParams): Observable<List<Visitable<*>>>
}
