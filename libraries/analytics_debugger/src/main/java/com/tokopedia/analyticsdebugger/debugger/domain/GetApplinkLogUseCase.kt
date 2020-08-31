package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

class GetApplinkLogUseCase @Inject
internal constructor(applinkLogRepository: ApplinkLogLocalRepository) : UseCase<List<Visitable<*>>>() {
    private val applinkLogRepository: ApplinkLogRepository

    init {
        this.applinkLogRepository = applinkLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return applinkLogRepository.get(requestParams)
    }
}
