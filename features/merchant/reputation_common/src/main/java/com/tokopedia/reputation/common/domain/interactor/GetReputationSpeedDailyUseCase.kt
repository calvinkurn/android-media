package com.tokopedia.reputation.common.domain.interactor

import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeedV2
import com.tokopedia.reputation.common.domain.repository.ReputationCommonRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetReputationSpeedDailyUseCase(private val reputationCommonRepository: ReputationCommonRepository):
        UseCase<ReputationSpeedV2>() {

    companion object {
        private const val SHOP_ID = "SHOP_ID"

        @JvmStatic
        fun createRequestParam(shopId: String) = RequestParams.create().apply { putString(SHOP_ID, shopId) }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ReputationSpeedV2> {
        return reputationCommonRepository.getStatisticSpeedDaily(requestParams.getString(SHOP_ID, null))
    }
}