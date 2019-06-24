package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.GMParamApiContant
import com.tokopedia.gm.common.data.source.cloud.GetShopStatusCloudSource
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 12/06/19.
 */
class GetShopStatusUseCase @Inject constructor(
        private val getShopStatusCloudSource: GetShopStatusCloudSource) : UseCase<ShopStatusModel>() {
    override fun createObservable(requestParams: RequestParams?): Observable<ShopStatusModel> {
        return getShopStatusCloudSource.getShopStatus(
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