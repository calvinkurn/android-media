package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class GetTopAdsLogDataUseCase @Inject
constructor(topAdsLogRepository: TopAdsLogLocalRepository) : UseCase<List<TopAdsLogDB>>() {
    private val topAdsLogRepository: TopAdsLogRepository

    init {
        this.topAdsLogRepository = topAdsLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<TopAdsLogDB>> {
        return topAdsLogRepository.get(requestParams)
    }
}
