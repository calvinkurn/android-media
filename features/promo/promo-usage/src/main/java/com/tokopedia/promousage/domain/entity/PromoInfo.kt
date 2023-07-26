package com.tokopedia.promousage.domain.entity

import androidx.annotation.StringDef

data class PromoInfo(
    val type: String = "",
    val title: String = ""
) {
    companion object {
        const val TYPE_TOP_BANNER = "top_banner"
        const val TYPE_PROMO_INFO = "promo_info"
        const val TYPE_BOTTOM_BANNER = "bottom_banner"
        const val TYPE_PROMO_VALIDITY = "promo_validity"
    }
}
