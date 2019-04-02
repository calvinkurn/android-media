package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

class TopAdsGetDataCreditUseCase @Inject
constructor(private val repository: TopAdsDashboardRepository) : UseCase<List<DataCredit>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<DataCredit>> {
        return repository.getDashboardCredit(requestParams)
    }
}
