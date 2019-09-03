package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireData
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireModel
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireRateModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetPMCancellationQuestionnaireDataUseCase @Inject constructor(
        private val getShopStatusUseCase: GetShopStatusUseCase
) : UseCase<PMCancellationQuestionnaireData>() {
    override fun createObservable(requestParams: RequestParams): Observable<PMCancellationQuestionnaireData> {
        return Observable.zip(
                getShopStatus(requestParams),
                getShopStatus(requestParams)
        ) { shopStatus, result2 ->
            PMCancellationQuestionnaireData(shopStatus)
        }
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