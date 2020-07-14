package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

class DeleteApplinkLogUseCase @Inject
internal constructor(applinkLogRepository: ApplinkLogLocalRepository) : UseCase<Boolean>() {
    private val applinkLogRepository: ApplinkLogRepository

    init {
        this.applinkLogRepository = applinkLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return applinkLogRepository.removeAll()
    }
}
