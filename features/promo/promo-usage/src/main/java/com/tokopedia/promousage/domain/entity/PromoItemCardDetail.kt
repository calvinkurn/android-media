package com.tokopedia.promousage.domain.entity

data class PromoItemCardDetail(
    val state: String = TYPE_INITIAL,
    val color: String = "",
    val iconUrl: String = "",
    val backgroundUrl: String = ""
) {
    companion object {
        const val TYPE_INITIAL = "initial"
        const val TYPE_SELECTED = "selected"
        const val TYPE_CLASHED = "clash"
    }
}
