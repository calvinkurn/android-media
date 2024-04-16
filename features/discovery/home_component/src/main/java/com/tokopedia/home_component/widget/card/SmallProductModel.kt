package com.tokopedia.home_component.widget.card

data class SmallProductModel(
    val bannerImageUrl: String,
    val title: Pair<String, TextStyle>,
    val subtitle: Pair<String, TextStyle>,
) {

    data class TextStyle(
        val isBold: Boolean = false,
        val textColor: String = "",
        val shouldRenderHtmlFormat: Boolean = false
    )
}
