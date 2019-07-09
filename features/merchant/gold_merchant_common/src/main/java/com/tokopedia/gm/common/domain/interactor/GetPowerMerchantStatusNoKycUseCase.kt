package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.constant.GMParamConstant
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetPowerMerchantStatusNoKycUseCase @Inject constructor(private val getShopStatusUseCase: GetShopStatusUseCase,
                                                             private val getShopScoreUseCase: GetShopScoreUseCase)
    : UseCase<Pair<GoldGetPmOsStatus, ShopScoreResult>>() {

    override fun createObservable(requestParams: RequestParams): Observable<Pair<GoldGetPmOsStatus, ShopScoreResult>> {
        return Observable.zip(
                getShopStatus(requestParams),
                getShopScore(requestParams)) { t1, t2 ->
            t1 to t2
        }
    }

    private fun getShopStatus(requestParams: RequestParams): Observable<GoldGetPmOsStatus> {
        return getShopStatusUseCase.createObservable(GetShopStatusUseCase.createRequestParams(requestParams.getString(
                GMParamConstant.SHOP_ID,""))).subscribeOn(Schedulers.io())
    }

    private fun getShopScore(requestParams: RequestParams): Observable<ShopScoreResult> {
        return getShopScoreUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
    }

    companion object {
        @JvmStatic
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(GMParamApiContant.SHOP_ID, shopId)
            }
        }
    }


}