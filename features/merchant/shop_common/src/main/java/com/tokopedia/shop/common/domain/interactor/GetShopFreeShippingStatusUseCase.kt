package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.shop.common.data.source.cloud.model.ShopFreeShippingStatus
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopFreeShippingStatusUseCase @Inject constructor(
    private val getShopFreeShippingInfoUseCase: GetShopFreeShippingInfoUseCase,
    private val getShopFreeShippingEligibilityUseCase: GetShopFreeShippingEligibilityUseCase
) {
    companion object {
        private const val PARAM_SHOP_ID = "shopIDs"

        fun createRequestParams(shopIds: List<Int>): RequestParams {
            return RequestParams().apply {
                putObject(PARAM_SHOP_ID, shopIds)
            }
        }
    }

    suspend fun execute(requestParams: RequestParams): ShopFreeShippingStatus {
        val active = getShopFreeShippingInfoUseCase.execute(requestParams).first().freeShipping.isActive
        val eligible = getShopFreeShippingEligibilityUseCase.execute(requestParams).first().status
        return ShopFreeShippingStatus(active, eligible)
    }
}