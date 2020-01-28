package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import com.tokopedia.user_identification_common.domain.usecase.GetUserProjectInfoUseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetPowerMerchantStatusUseCase @Inject constructor(private val getShopStatusUseCase: GetShopStatusUseCase,
                                                        private val getUserProjectInfoUseCase: GetUserProjectInfoUseCase,
                                                        private val getShopScoreUseCase: GetShopScoreUseCase)
    : UseCase<PowerMerchantStatus>() {

    override fun createObservable(requestParams: RequestParams): Observable<PowerMerchantStatus> {
        return Observable.zip(
                getShopStatus(requestParams),
                getKycStatus(),
                getShopScore(requestParams)) { t1, t2, t3 ->
            PowerMerchantStatus(t1, t2, t3)
        }
    }

    private fun getShopStatus(requestParams: RequestParams): Observable<GoldGetPmOsStatus> {
        return getShopStatusUseCase.createObservable(GetShopStatusUseCase.createRequestParams(
                requestParams.getString(GMParamApiContant.SHOP_ID, "")))
                .subscribeOn(Schedulers.io())
    }

    private fun getShopScore(requestParams: RequestParams): Observable<ShopScoreResult> {
        return getShopScoreUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
    }

    private fun getKycStatus(): Observable<KycUserProjectInfoPojo> {
        val requestParams = createKycStatusRequestParam()
        return getUserProjectInfoUseCase.execute(requestParams.parameters)
    }

    companion object {
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(GMParamApiContant.SHOP_ID, shopId)
            }
        }

        fun createKycStatusRequestParam(): RequestParams {
            return RequestParams.create().apply {
                putInt(PROJECT_ID, PROJECT_ID_POWER_MERCHANT_KYC)
            }
        }

        const val PROJECT_ID = "projectId"
        const val PROJECT_ID_POWER_MERCHANT_KYC = 10
    }
}