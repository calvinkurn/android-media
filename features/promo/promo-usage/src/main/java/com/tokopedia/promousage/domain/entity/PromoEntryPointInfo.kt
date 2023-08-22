package com.tokopedia.promousage.domain.entity

data class PromoEntryPointInfo(
    val iconUrl: String = "",
    val messages: List<String> = emptyList(),
    val color: String = "",
    val isClickable: Boolean = false
) {
    companion object {
        const val COLOR_GREEN = "green"
        const val COLOR_GREY = "grey"
    }
}
