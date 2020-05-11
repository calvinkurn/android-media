package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/17/18.
 */
class DeleteFpmLogUseCase @Inject
internal constructor(fpmLogRepositoory: FpmLogLocalRepository) : UseCase<Boolean>() {
    private val fpmLogRepository: FpmLogRepository

    init {
        this.fpmLogRepository = fpmLogRepositoory
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return fpmLogRepository.removeAll()
    }
}
