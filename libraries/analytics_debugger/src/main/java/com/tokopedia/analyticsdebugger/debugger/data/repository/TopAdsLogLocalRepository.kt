package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.data.mapper.TopAdsLogMapper
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsLogDBSource
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsVerificationNetworkSource
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

class TopAdsLogLocalRepository @Inject
internal constructor(private val topAdsLogDBSource: TopAdsLogDBSource,
                     private val topAdsLogMapper: TopAdsLogMapper,
                     private val topAdsVerificationNetworkSource: TopAdsVerificationNetworkSource) : TopAdsLogRepository {

    override fun removeAll(): Observable<Boolean> {
        return topAdsLogDBSource.deleteAll()
    }

    override fun get(parameters: RequestParams): Observable<List<Visitable<*>>> {
        return topAdsLogDBSource.getData(parameters.parameters)
                .flatMap<List<TopAdsLogDB>>({ logDbList -> topAdsVerificationNetworkSource.appendVerificationStatus(logDbList)})
                .flatMapIterable { topAdsLogDBS -> topAdsLogDBS }
                .flatMap<Visitable<*>>(topAdsLogMapper).toList()
    }
}
