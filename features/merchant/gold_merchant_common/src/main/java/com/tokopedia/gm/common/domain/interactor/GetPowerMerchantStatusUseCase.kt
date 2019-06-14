package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo
import com.tokopedia.user_identification_common.usecase.GetApprovalStatusUseCase
import rx.Observable

import javax.inject.Inject

class GetPowerMerchantStatusUseCase @Inject constructor(private val getShopStatusUseCase: GetShopStatusUseCase,
                                                        private val getApprovalStatusUseCase: GetApprovalStatusUseCase,
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

    private fun getShopStatus(requestParams: RequestParams): Observable<ShopStatusModel> {
        return getShopStatusUseCase.createObservable(requestParams)
    }

    private fun getShopScore(requestParams: RequestParams): Observable<ShopScoreResult> {
        return getShopScoreUseCase.createObservable(requestParams)
    }

    private fun getKycStatus(): Observable<GetApprovalStatusPojo> {
        return getApprovalStatusUseCase.execute(GetApprovalStatusUseCase.getRequestParam())
    }

    companion object {
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(GMParamApiContant.SHOP_ID, shopId)
            }
        }
    }


}