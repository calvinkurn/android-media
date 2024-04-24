package com.tokopedia.home_component.widget.card

data class SmallProductModel(
    val bannerImageUrl: String,
    val ribbon: Ribbon? = null,
    val stockBar: StockBar = StockBar(),
    val title: Pair<String, TextStyle>,
    val subtitle: Pair<String, TextStyle>,
) {

    data class Ribbon(
        val text: String,
        val type: Type,
    ) {

        sealed class Type {
            object Red : Type()
            object Gold : Type()
        }
    }

    data class TextStyle(
        val isBold: Boolean = false,
        val textColor: String = "",
        val shouldRenderHtmlFormat: Boolean = false
    )

    data class StockBar(
        val isEnabled: Boolean = false,
        val percentage: Int = 0
    )
}
