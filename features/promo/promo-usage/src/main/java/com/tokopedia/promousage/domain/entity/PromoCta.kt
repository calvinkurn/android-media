package com.tokopedia.promousage.domain.entity

import androidx.annotation.StringDef

data class PromoCta(
    @PromoCtaType val type: String = TYPE_UNKNOWN,
    val text: String = "",
    val appLink: String = ""
) {

    companion object {

        const val TYPE_REGISTER_GOPAY_LATER_CICIL = "register_gpl_cicil"
        const val TYPE_UNKNOWN = "unknown"
    }
}

@StringDef(PromoCta.TYPE_REGISTER_GOPAY_LATER_CICIL)
@Retention(AnnotationRetention.SOURCE)
annotation class PromoCtaType
