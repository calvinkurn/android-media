package com.tokopedia.promousage.domain.entity

import androidx.annotation.StringDef

data class PromoCardDetail(
    @PromoCardDetailType val state: String = TYPE_INITIAL,
    val color: String = "",
    val iconUrl: String = "",
    val backgroundUrl: String = ""
) {

    companion object {

        const val TYPE_INITIAL = "initial"
        const val TYPE_SELECTED = "selected"
    }
}

@StringDef(
    PromoCardDetail.TYPE_INITIAL,
    PromoCardDetail.TYPE_SELECTED
)
@Retention(AnnotationRetention.SOURCE)
annotation class PromoCardDetailType
