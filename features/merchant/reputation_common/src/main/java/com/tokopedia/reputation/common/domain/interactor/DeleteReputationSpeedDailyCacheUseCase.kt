package com.tokopedia.reputation.common.domain.interactor

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase
import com.tokopedia.reputation.common.constant.ReputationCommonUrl
import com.tokopedia.usecase.RequestParams
import rx.Observable

class DeleteReputationSpeedDailyCacheUseCase: CacheApiDataDeleteUseCase(){
    fun createObservable(): Observable<Boolean> = createObservable(RequestParams.create())

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return super.createObservable(CacheApiDataDeleteUseCase
                .createParams(ReputationCommonUrl.BASE_URL, ReputationCommonUrl.STATISTIC_SPEED_URL_V2))
    }
}