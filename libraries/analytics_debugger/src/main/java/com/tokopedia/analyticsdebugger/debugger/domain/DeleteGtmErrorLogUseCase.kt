package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmErrorLogLocalRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

class DeleteGtmErrorLogUseCase @Inject
internal constructor(private val gtmErrorLogLocalRepository: GtmErrorLogLocalRepository) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return gtmErrorLogLocalRepository.removeAll()
    }
}
