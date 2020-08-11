package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.data.mapper.TopAdsLogMapper
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class GetTopAdsLogUseCase @Inject
internal constructor(topAdsLogRepository: TopAdsLogLocalRepository,
                     private val topAdsLogMapper: TopAdsLogMapper) : UseCase<List<Visitable<*>>>() {
    private val topAdsLogRepository: TopAdsLogRepository

    init {
        this.topAdsLogRepository = topAdsLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return topAdsLogRepository.get(requestParams)
                .flatMapIterable { topAdsLogDBS -> topAdsLogDBS }
                .flatMap<Visitable<*>>(topAdsLogMapper).toList()
    }
}
