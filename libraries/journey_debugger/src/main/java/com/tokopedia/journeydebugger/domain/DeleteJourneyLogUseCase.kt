package com.tokopedia.journeydebugger.domain

import com.tokopedia.journeydebugger.data.repository.JourneyLogLocalRepository
import com.tokopedia.journeydebugger.data.repository.JourneyLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

class DeleteJourneyLogUseCase @Inject
internal constructor(journeyLogRepository: JourneyLogLocalRepository) : UseCase<Boolean>() {
    private val journeyLogRepository: JourneyLogRepository

    init {
        this.journeyLogRepository = journeyLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return journeyLogRepository.removeAll()
    }
}
