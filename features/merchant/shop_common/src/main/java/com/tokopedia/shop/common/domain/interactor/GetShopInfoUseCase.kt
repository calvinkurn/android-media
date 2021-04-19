package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by User on 9/8/2017.
 */
class GetShopInfoUseCase(private val shopRepository: ShopCommonRepository) : UseCase<ShopInfo>() {
    override fun createObservable(requestParams: RequestParams): Observable<ShopInfo> {
        val shopId = requestParams.getString(SHOP_ID, "0")
        val userId = requestParams.getString(USER_ID, "0")
        val deviceId = requestParams.getString(DEVICE_ID, "")
        return shopRepository.getShopInfo(shopId, userId, deviceId)
    }

    companion object {
        private const val SHOP_ID = "SHOP_ID"
        private const val USER_ID = "USER_ID"
        private const val DEVICE_ID = "DEVICE_ID"
        fun createRequestParam(shopId: String?, userId: String?, deviceId: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_ID, shopId)
            requestParams.putString(USER_ID, userId)
            requestParams.putString(DEVICE_ID, deviceId)
            return requestParams
        }
    }
}