package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by User on 9/8/2017.
 */
class GetShopInfoByDomainUseCase(private val shopRepository: ShopCommonRepository) : UseCase<ShopInfo>() {
    override fun createObservable(requestParams: RequestParams): Observable<ShopInfo> {
        val shopDomain = requestParams.getString(SHOP_DOMAIN, "")
        val userId = requestParams.getString(USER_ID, "0")
        val deviceId = requestParams.getString(DEVICE_ID, "")
        return shopRepository.getShopInfoByDomain(shopDomain, userId, deviceId)
    }

    companion object {
        private const val SHOP_DOMAIN = "SHOP_DOMAIN"
        private const val USER_ID = "USER_ID"
        private const val DEVICE_ID = "DEVICE_ID"
        @JvmStatic
        fun createRequestParam(shopDomain: String?, userId: String?, deviceId: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_DOMAIN, shopDomain)
            requestParams.putString(USER_ID, userId)
            requestParams.putString(DEVICE_ID, deviceId)
            return requestParams
        }
    }
}