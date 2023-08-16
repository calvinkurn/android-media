package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt

data class SliderImageTextUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    val backgroundColor: Int? = null,
    val items: List<ItemSliderImageText>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName) {

    data class ItemSliderImageText(
        val image: String,
        val textHighlight: String,
        val textTitle: String,
        val textDescription: String,
        val textColor: Int,
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun dummySliderImageText() = SliderImageTextUiModel(
            "dummy", "", "", "#F0F3F7".stringHexColorParseToInt(),
            items = listOf(
                ItemSliderImageText(
                    "https://images.unsplash.com/photo-1601944177325-f8867652837f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2077&q=80",
                    "Highlight has a max. 1 line",
                    "The title must have 2 lines, make sure the min & max characters!",
                    "The description must have 4 lines! so please give as much context as possible but make it concise! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
                    "#6D7588".stringHexColorParseToInt()
                ),
                ItemSliderImageText(
                    "https://images.unsplash.com/photo-1601944177325-f8867652837f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2077&q=80",
                    "Highlight has a max. 1 line",
                    "The title must have 2 lines, make sure the min & max characters!",
                    "The description must have 4 lines! so please give as much context as possible but make it concise! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
                    "#6D7588".stringHexColorParseToInt()
                ),
                ItemSliderImageText(
                    "https://images.unsplash.com/photo-1601944177325-f8867652837f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2077&q=80",
                    "Highlight has a max. 1 line",
                    "The title must have 2 lines, make sure the min & max characters!",
                    "The description must have 4 lines! so please give as much context as possible but make it concise! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
                    "#6D7588".stringHexColorParseToInt()
                )
            )
        )
    }
}
