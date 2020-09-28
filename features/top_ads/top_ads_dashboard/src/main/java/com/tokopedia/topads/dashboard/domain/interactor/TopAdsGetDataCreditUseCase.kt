package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class TopAdsGetDataCreditUseCase @Inject
constructor(private val repository: TopAdsDashboardRepository) : UseCase<List<DataCredit>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<DataCredit>> {
        return repository.getDashboardCredit(requestParams)
    }
}
