package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.catalogcommon.util.colorMapping

data class SliderImageTextUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? = null,
    override val widgetTextColor: Int? = null,
    override val darkMode: Boolean = false,
    val items: List<ItemSliderImageText>,
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {

    data class ItemSliderImageText(
        val image: String,
        val textHighlight: String,
        val textTitle: String,
        val textDescription: String,
        val textHighlightColor: Int,
        val textTitleColor: Int,
        val textDescriptionColor: Int
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        private const val dummyDarkMode = true
        fun dummySliderImageText() = SliderImageTextUiModel(
            "dummy", "", "", "#000000".stringHexColorParseToInt(),
            items = listOf(
                ItemSliderImageText(
                    "https://images.unsplash.com/photo-1601944177325-f8867652837f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2077&q=80",
                    "Highlight 1 has a max. 1 line",
                    "The title 1 must have 2 lines, make sure the min & max characters!",
                    "The description 1 must have 4 lines! so please give as much context as possible but make it concise! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
                    textHighlightColor = colorMapping(dummyDarkMode,"#AEB2BF","#6D7588"),
                    textTitleColor = colorMapping(dummyDarkMode,"#F5F6FF","#212121"),
                    textDescriptionColor = colorMapping(dummyDarkMode,"#AEB2BF","#6D7588")
                ),
                ItemSliderImageText(
                    "https://images.unsplash.com/photo-1601944179066-29786cb9d32a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
                    "Highlight 2 has a max. 1 line",
                    "The title 2 must have 2 lines, make sure the min & max characters!",
                    "The description 2 must have 4 lines! so please give as much context as possible but make it concise! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
                    textHighlightColor = colorMapping(dummyDarkMode,"#AEB2BF","#6D7588"),
                    textTitleColor = colorMapping(dummyDarkMode,"#F5F6FF","#212121"),
                    textDescriptionColor = colorMapping(dummyDarkMode,"#AEB2BF","#6D7588")
                ),
                ItemSliderImageText(
                    "https://plus.unsplash.com/premium_photo-1661779632194-6cf32a9b5325?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1074&q=80",
                    "Highlight 3 has a max. 1 line",
                    "The title 3 must have 2 lines, make sure the min & max characters!",
                    "The description 3 must have 4 lines! so please give as much context as possible but make it concise! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
                    textHighlightColor = colorMapping(dummyDarkMode,"#AEB2BF","#6D7588"),
                    textTitleColor = colorMapping(dummyDarkMode,"#F5F6FF","#212121"),
                    textDescriptionColor = colorMapping(dummyDarkMode,"#AEB2BF","#6D7588")
                )
            )
        )
    }
}
