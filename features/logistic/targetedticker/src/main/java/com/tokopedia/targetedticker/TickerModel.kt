package com.tokopedia.targetedticker

data class TickerModel(
    val item: List<TickerItem> = listOf()
) {
    data class TickerItem(
        val id: Long = 0,
        val type: Int = -1,
        val title: String = "",
        val content: String = "",
        val linkUrl: String = "",
        val priority: Long = -1
    )
}
