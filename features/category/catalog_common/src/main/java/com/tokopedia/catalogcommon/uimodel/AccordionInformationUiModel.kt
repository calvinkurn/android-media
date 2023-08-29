package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.catalogcommon.util.textColorMapping

data class AccordionInformationUiModel(
    override var idWidget: String,
    override var widgetType: String,
    override var widgetName: String,
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val titleWidget: String,
    val contents: List<ItemAccordionInformationUiModel>
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }


    data class ItemAccordionInformationUiModel(
        val title: String,
        val description: String,
        val textTitleColor: Int? = null,
        val textDescriptionColor: Int? = null,
        val arrowColor: Int? =null
    ) {

    }


    companion object {

        private const val dummyDarkMode = true

        fun dummyAccordion() = AccordionInformationUiModel(
            "dummy", "", "", "#000000".stringHexColorParseToInt(),
            widgetTextColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
            titleWidget = "Section Title Maximal 1 line",
            contents = listOf(
                ItemAccordionInformationUiModel(
                    "Highlight 1 has a max. 1 line",
                    "The title 1 must have 2 lines, make sure the min & max characters!",
                    textTitleColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textDescriptionColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    arrowColor = textColorMapping(dummyDarkMode, "#E9EBF5", "#2E3137")
                ),
                ItemAccordionInformationUiModel(
                    "Highlight 1 has a max. 1 line",
                    "The title 1 must have 2 lines, make sure the min & max characters!",
                    textTitleColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textDescriptionColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    arrowColor = textColorMapping(dummyDarkMode, "#E9EBF5", "#2E3137")
                ),
                ItemAccordionInformationUiModel(
                    "Highlight 1 has a max. 1 line",
                    "The title 1 must have 2 lines, make sure the min & max characters!",
                    textTitleColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textDescriptionColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    arrowColor = textColorMapping(dummyDarkMode, "#E9EBF5", "#2E3137")
                )
            )
        )
    }
}
