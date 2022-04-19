package com.tokopedia.purchase_platform.common.feature.promonoteligible

data class NotEligiblePromoHolderdata(
        var promoTitle: String = "",
        var promoCode: String = "",
        var shopName: String = "",
        var shopBadge: String = "",
        var iconType: Int = 0,
        var errorMessage: String = "",
        var showShopSection: Boolean = false
) {
    companion object {
        @JvmStatic
        val TYPE_ICON_GLOBAL = 1
    }
}