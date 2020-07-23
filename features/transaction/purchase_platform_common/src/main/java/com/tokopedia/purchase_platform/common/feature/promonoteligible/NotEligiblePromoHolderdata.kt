package com.tokopedia.purchase_platform.common.feature.promonoteligible

/**
 * Created by Irfan Khoirul on 2019-06-19.
 */

data class NotEligiblePromoHolderdata(
        var promoTitle: String = "",
        var promoCode: String = "",
        var shopName: String = "",
        var iconType: Int = 0,
        var errorMessage: String = "",
        var showShopSection: Boolean = false
) {
    companion object {
        @JvmStatic
        val TYPE_ICON_GLOBAL = 1

        @JvmStatic
        val TYPE_ICON_OFFICIAL_STORE = 2

        @JvmStatic
        val TYPE_ICON_POWER_MERCHANT = 3
    }
}