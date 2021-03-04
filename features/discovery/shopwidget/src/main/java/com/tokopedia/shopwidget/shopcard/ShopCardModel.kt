package com.tokopedia.shopwidget.shopcard

import com.tokopedia.discovery.common.constants.SearchConstant.ShopStatus.*
import com.tokopedia.kotlin.model.ImpressHolder

data class ShopCardModel(
        val id: String = "",
        val name: String = "",
        val domain: String = "",
        val url: String = "",
        val applink: String = "",
        val image: String = "",
        val image300: String = "",
        val description: String = "",
        val tagLine: String = "",
        val location: String = "",
        val totalTransaction: String = "",
        val totalFavorite: String = "",
        val goldShop: Int = 0,
        val isOwner: Int = 0,
        val rateSpeed: Int = 0,
        val rateAccuracy: Int = 0,
        val rateService: Int = 0,
        val status: Int = 0,
        val productList: List<ShopItemProduct> = listOf(),
        val voucher: ShopItemVoucher = ShopItemVoucher(),
        val lucky: String = "",
        val reputationImageUri: String = "",
        val reputationScore: Int = 0,
        val isOfficial: Boolean = false,
        val gaKey: String = "",
        val isRecommendation: Boolean = false,
        val impressHolder: ImpressHolder? = null
) {

    val isGoldShop = goldShop == KEY_SHOP_IS_GOLD

    val isClosed = status == KEY_SHOP_STATUS_CLOSED

    val isModerated = status == KEY_SHOP_STATUS_MODERATED

    val isInactive = status == KEY_SHOP_STATUS_INACTIVE

    data class ShopItemProduct(
            val id: Int = 0,
            val name: String = "",
            val url: String = "",
            val applink: String = "",
            val price: Int = 0,
            val priceFormat: String = "",
            val imageUrl: String = "",
            val isRecommendation: Boolean = false,
            val impressHolder: ImpressHolder? = null
    )

    data class ShopItemVoucher(
            val freeShipping: Boolean = false,
            val cashback: ShopItemVoucherCashback = ShopItemVoucherCashback()
    )

    data class ShopItemVoucherCashback(
            val cashbackValue: Int = 0,
            val isPercentage: Boolean = false
    )
}