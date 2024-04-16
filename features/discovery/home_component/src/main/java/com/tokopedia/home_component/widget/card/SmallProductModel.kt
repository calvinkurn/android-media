package com.tokopedia.home_component.widget.card

data class SmallProductModel(
    val bannerImageUrl: String,
    val ribbon: Ribbon? = null,
    val title: Pair<String, TextStyle>,
    val subtitle: Pair<String, TextStyle>,
) {

    data class Ribbon(
        val text: String,
        val type: Type,
    ) {

        sealed class Type {
            object Red : Type()
            object Yellow : Type()
        }
    }

    data class TextStyle(
        val isBold: Boolean = false,
        val textColor: String = "",
        val shouldRenderHtmlFormat: Boolean = false
    )
}
