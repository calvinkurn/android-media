package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class GetGtmLogUseCase @Inject
internal constructor(gtmLogRepository: GtmLogLocalRepository) : UseCase<List<Visitable<*>>>() {
    private val gtmLogRepository: GtmLogRepository

    init {
        this.gtmLogRepository = gtmLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return gtmLogRepository.get(requestParams)
    }
}
