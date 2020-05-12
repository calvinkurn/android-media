package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class GetFpmLogUseCase @Inject
internal constructor(fpmLogRepository: FpmLogLocalRepository) : UseCase<List<Visitable<*>>>() {
    private val fpmLogRepository: FpmLogRepository

    init {
        this.fpmLogRepository = fpmLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return fpmLogRepository.get(requestParams)
    }
}
