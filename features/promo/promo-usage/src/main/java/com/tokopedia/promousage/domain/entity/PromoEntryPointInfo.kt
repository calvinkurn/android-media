package com.tokopedia.promousage.domain.entity

data class PromoEntryPointInfo(
    val iconUrl: String = "",
    val messages: List<String> = emptyList(),
    val color: String = COLOR_GREY,
    val isClickable: Boolean = false,

    val isSuccess: Boolean = false,
    val statusCode: String = ""
) {
    companion object {
        const val COLOR_GREEN = "green"
        const val COLOR_GREY = "grey"

        const val ICON_URL_ENTRY_POINT_NO_ITEM_SELECTED = "https://images.tokopedia.net/img/promo/icon/Product.png"
        const val ICON_URL_ENTRY_POINT_NO_ITEM_SELECTED_NEW = "https://images.tokopedia.net/img/promo/icon/Coupon-1.png"
        const val ICON_URL_ENTRY_POINT_APPLIED = "https://images.tokopedia.net/img/promo/icon/Applied.png"
    }
}
