package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.ApplinkLogDB
import com.tokopedia.analyticsdebugger.debugger.data.mapper.ApplinkLogMapper
import com.tokopedia.analyticsdebugger.debugger.data.source.ApplinkLogDBSource
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class ApplinkLogLocalRepository @Inject
internal constructor(private val applinkLogDBSource: ApplinkLogDBSource,
                     private val applinkLogMapper: ApplinkLogMapper) : ApplinkLogRepository {

    override fun insert(data: ApplinkLogModel): Observable<Boolean> {
        return applinkLogDBSource.insertAll(data)
    }

    override fun removeAll(): Observable<Boolean> {
        return applinkLogDBSource.deleteAll()
    }

    override fun get(parameters: RequestParams): Observable<List<Visitable<*>>> {
        return applinkLogDBSource.getData(parameters.parameters)
                .flatMapIterable { ApplinkLogDBS -> ApplinkLogDBS }.flatMap<Visitable<*>>(applinkLogMapper).toList()
    }
}
