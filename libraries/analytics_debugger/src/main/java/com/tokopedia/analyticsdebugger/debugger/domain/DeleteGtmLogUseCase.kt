package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/17/18.
 */
class DeleteGtmLogUseCase @Inject
internal constructor(gtmLogRepositoory: GtmLogLocalRepository) : UseCase<Boolean>() {
    private val gtmLogRepository: GtmLogRepository

    init {
        this.gtmLogRepository = gtmLogRepositoory
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return gtmLogRepository.removeAll()
    }
}
