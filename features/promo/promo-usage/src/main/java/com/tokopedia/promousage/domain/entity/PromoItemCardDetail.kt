package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoItemCardDetail(
    val state: String = TYPE_INITIAL,
    val color: String = "",
    val iconUrl: String = "",
    val backgroundUrl: String = ""
) : Parcelable {
    companion object {
        const val TYPE_INITIAL = "initial"
        const val TYPE_SELECTED = "selected"
        const val TYPE_CLASHED = "clash"
    }
}
