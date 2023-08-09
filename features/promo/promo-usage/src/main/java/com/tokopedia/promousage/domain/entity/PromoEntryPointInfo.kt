package com.tokopedia.promousage.domain.entity

data class PromoEntryPointInfo(
    val messages: List<String> = emptyList(),
    val iconUrl: String = "",
    val state: String = "",
    val clickable: Boolean = false
) {
    companion object {
        const val STATE_GREEN = "green"
        const val STATE_GREY = "grey"
    }
}
