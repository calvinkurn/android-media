package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.mapper.GtmLogMapper
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

/**
 * @author okasurya on 5/16/18.
 */
class GtmLogLocalRepository @Inject
internal constructor(private val gtmLogDBSource: GtmLogDBSource,
                     private val gtmLogMapper: GtmLogMapper) : GtmLogRepository {

    override fun insert(data: AnalyticsLogData): Observable<Boolean> {
        return gtmLogDBSource.insertAll(data)
    }

    override fun removeAll(): Observable<Boolean> {
        return gtmLogDBSource.deleteAll()
    }

    override fun get(parameters: RequestParams): Observable<List<Visitable<*>>> {
        return gtmLogDBSource.getData(parameters.parameters)
                .flatMapIterable { gtmLogDBS -> gtmLogDBS }.flatMap<Visitable<*>>(gtmLogMapper).toList()
    }
}
