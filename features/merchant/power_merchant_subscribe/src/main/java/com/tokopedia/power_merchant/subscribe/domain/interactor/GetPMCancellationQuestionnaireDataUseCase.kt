package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaire
import com.tokopedia.power_merchant.subscribe.domain.model.PMCancellationQuestionnaireDataUseCaseModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetPMCancellationQuestionnaireDataUseCase @Inject constructor(
        private val getShopStatusUseCase: GetShopStatusUseCase,
        private val getGoldCancellationsQuestionnaireUseCase: GetGoldCancellationsQuestionaireUseCase
) : UseCase<PMCancellationQuestionnaireDataUseCaseModel>() {
    override fun createObservable(requestParams: RequestParams): Observable<PMCancellationQuestionnaireDataUseCaseModel> {
        return Observable.zip(
                getShopStatus(requestParams),
                getGoldCancellationQuestionnaire()
        ) { shopStatus, goldCancellationQuestionnaire ->
            PMCancellationQuestionnaireDataUseCaseModel(shopStatus,goldCancellationQuestionnaire)
        }
    }

    private fun getGoldCancellationQuestionnaire(): Observable<GoldCancellationsQuestionaire> {
        return getGoldCancellationsQuestionnaireUseCase.createObservable(
                RequestParams.EMPTY
        ).subscribeOn(Schedulers.io())
    }

    private fun getShopStatus(requestParams: RequestParams): Observable<GoldGetPmOsStatus> {
        return getShopStatusUseCase.createObservable(
                GetShopStatusUseCase.createRequestParams(
                        requestParams.getString(GMParamApiContant.SHOP_ID, ""))
        ).subscribeOn(Schedulers.io())
    }

    companion object {
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(GMParamApiContant.SHOP_ID, shopId)
            }
        }
    }
}