package com.tokopedia.promousage.domain.entity

import androidx.annotation.StringDef

data class PromoInfo(
    @PromoInfoType val type: String = TYPE_UNKNOWN,
    val title: String = ""
) {

    companion object {
        const val TYPE_TOP_BANNER = "top_banner"
        const val TYPE_PROMO_INFO = "promo_info"
        const val TYPE_BOTTOM_BANNER = "bottom_banner"
        const val TYPE_PROMO_VALIDITY = "promo_validity"
        const val TYPE_UNKNOWN = "unknown"
    }
}

@StringDef(
    PromoInfo.TYPE_TOP_BANNER,
    PromoInfo.TYPE_PROMO_INFO,
    PromoInfo.TYPE_BOTTOM_BANNER,
    PromoInfo.TYPE_PROMO_VALIDITY,
    PromoInfo.TYPE_UNKNOWN
)
@Retention(AnnotationRetention.SOURCE)
annotation class PromoInfoType
