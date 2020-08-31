package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.FpmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.mapper.FpmLogMapper
import com.tokopedia.analyticsdebugger.debugger.data.source.FpmLogDBSource
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class FpmLogLocalRepository @Inject
internal constructor(private val fpmLogDBSource: FpmLogDBSource,
                     private val fpmLogMapper: FpmLogMapper) : FpmLogRepository {

    override val allData: Observable<List<Visitable<*>>>
        get() = fpmLogDBSource.allData
                .flatMapIterable { fpmLogDBS -> fpmLogDBS }.flatMap<Visitable<*>>(fpmLogMapper).toList()

    override fun insert(data: PerformanceLogModel): Observable<Boolean> {
        return fpmLogDBSource.insertAll(data)
    }

    override fun removeAll(): Observable<Boolean> {
        return fpmLogDBSource.deleteAll()
    }

    override fun get(parameters: RequestParams): Observable<List<Visitable<*>>> {
        return fpmLogDBSource.getData(parameters.parameters)
                .flatMapIterable { fpmLogDBS -> fpmLogDBS }.flatMap<Visitable<*>>(fpmLogMapper).toList()
    }
}
