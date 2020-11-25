package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/17/18.
 */
class DeleteTopAdsLogUseCase @Inject
internal constructor(topAdsLogRepositoory: TopAdsLogLocalRepository) : UseCase<Boolean>() {
    private val topAdsLogRepository: TopAdsLogRepository

    init {
        this.topAdsLogRepository = topAdsLogRepositoory
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return topAdsLogRepository.removeAll()
    }
}
