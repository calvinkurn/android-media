package com.tokopedia.home_component.model

data class LabelGroup(
    val title: String = "",
    val position: String = "",
    val type: String = "",
    val url: String = "",
    val styles: List<Style> = emptyList()
) {

    data class Style(
        val key: String = "",
        val value: String = "",
    )
}
