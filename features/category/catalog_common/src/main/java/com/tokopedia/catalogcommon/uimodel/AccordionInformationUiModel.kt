package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt

data class AccordionInformationUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? =null,
    override val widgetTextColor: Int? =null,
    val titleWidget: String,
    val contents: List<ItemAccordionInformationUiModel>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }


    data class ItemAccordionInformationUiModel(
        val title: String,
        val description: String,
        val textColor: Int? = null
    )


    companion object {
        fun dummyAccordion() = AccordionInformationUiModel(
            "dummy", "", "", "#FFFFFF".stringHexColorParseToInt(),
            titleWidget = "Section Title Maximal 1 line",
            contents = listOf(
                ItemAccordionInformationUiModel(
                    "Highlight 1 has a max. 1 line",
                    "The title 1 must have 2 lines, make sure the min & max characters!",
                    "#212121".stringHexColorParseToInt()
                ),
                ItemAccordionInformationUiModel(
                    "Highlight 1 has a max. 1 line",
                    "The title 1 must have 2 lines, make sure the min & max characters!",
                    "#212121".stringHexColorParseToInt()
                ),
                ItemAccordionInformationUiModel(
                    "Highlight 1 has a max. 1 line",
                    "The title 1 must have 2 lines, make sure the min & max characters!",
                    "#212121".stringHexColorParseToInt()
                )
            )
        )
    }
}
