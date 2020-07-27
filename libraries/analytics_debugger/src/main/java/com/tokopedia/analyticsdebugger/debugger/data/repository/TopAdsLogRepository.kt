package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel
import com.tokopedia.usecase.RequestParams

import rx.Observable

interface TopAdsLogRepository {

    fun removeAll(): Observable<Boolean>

    operator fun get(parameters: RequestParams): Observable<List<TopAdsLogDB>>
}
