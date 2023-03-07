package com.tokopedia.journeydebugger.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.journeydebugger.data.repository.JourneyLogLocalRepository
import com.tokopedia.journeydebugger.data.repository.JourneyLogRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

class GetJourneyLogUseCase @Inject
internal constructor(journeyLogRepository: JourneyLogLocalRepository) : UseCase<List<Visitable<*>>>() {
    private val journeyLogRepository: JourneyLogRepository

    init {
        this.journeyLogRepository = journeyLogRepository
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return journeyLogRepository.get(requestParams)
    }
}
