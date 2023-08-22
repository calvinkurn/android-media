package com.tokopedia.promousage.domain.entity

data class PromoItemInfo(
    val type: String = "",
    val icon: String = "",
    val title: String = ""
) {
    companion object {
        const val TYPE_TOP_BANNER = "top_banner"
        const val TYPE_PROMO_INFO = "promo_info"
        const val TYPE_BOTTOM_BANNER = "bottom_banner"
        const val TYPE_PROMO_VALIDITY = "promo_validity"

        const val ICON_DOT = "DOT"
        const val ICON_NONE = "NONE"
    }
}
