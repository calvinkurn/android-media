package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.GetShopScoreCloudSource
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetShopScoreUseCase @Inject constructor(private val getShopScoreCloudSource: GetShopScoreCloudSource)
    : UseCase<ShopScoreResult>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ShopScoreResult> {
        return getShopScoreCloudSource.getScoreInfo(
                requestParams?.getString(GMParamApiContant.SHOP_ID, "0")
                        ?: "0"
        )
    }

    companion object {
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(GMParamApiContant.SHOP_ID, shopId)
            }
        }
    }
}
