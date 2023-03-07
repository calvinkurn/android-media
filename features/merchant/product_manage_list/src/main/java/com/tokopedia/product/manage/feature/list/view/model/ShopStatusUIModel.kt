package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.ShopInfo

data class ShopStatusUIModel(
    val shopStatus: String,
    val statusTitle: String,
    val statusMessage: String,
    val tickerType: String
) {
    companion object {
        const val ON_MODERATED_STAGE = 3
        const val ON_MODERATED_PERMANENTLY = 5

        fun mapperShopStatusResponse(shopInfo: ShopInfo?): ShopStatusUIModel {
            return ShopStatusUIModel(
                shopStatus = shopInfo?.statusInfo?.shopStatus.orEmpty(),
                statusTitle = shopInfo?.statusInfo?.statusTitle.orEmpty(),
                statusMessage = shopInfo?.statusInfo?.statusMessage.orEmpty(),
                tickerType = shopInfo?.statusInfo?.tickerType.orEmpty()
            )
        }
    }

    fun isOnModerationMode(): Boolean {
        val status = shopStatus.toIntOrZero()
        return status == ON_MODERATED_STAGE || status == ON_MODERATED_PERMANENTLY
    }
}
