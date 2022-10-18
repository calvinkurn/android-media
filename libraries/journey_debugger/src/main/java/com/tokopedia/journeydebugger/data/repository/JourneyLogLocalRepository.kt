package com.tokopedia.journeydebugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.journeydebugger.data.mapper.JourneyLogMapper
import com.tokopedia.journeydebugger.data.source.JourneyLogDBSource
import com.tokopedia.journeydebugger.domain.model.JourneyLogModel
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

import rx.Observable

class JourneyLogLocalRepository @Inject
internal constructor(private val applinkLogDBSource: JourneyLogDBSource,
                     private val applinkLogMapper: JourneyLogMapper) : JourneyLogRepository {

    override fun insert(data: JourneyLogModel): Observable<Boolean> {
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
