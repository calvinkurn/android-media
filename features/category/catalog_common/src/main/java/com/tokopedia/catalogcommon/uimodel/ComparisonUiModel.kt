package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class ComparisonUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val content: MutableList<ComparisonContent> = mutableListOf()
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {

    data class ComparisonContent(
        val id: String,
        val imageUrl: String,
        val productTitle: String,
        val price: String,
        val comparisonSpecs: List<ComparisonSpec>,
        val topComparisonSpecs: List<ComparisonSpec>,
        var productTextColor: Int? = null,
        var titleHeight: Int = -1
    )

    data class ComparisonSpec(
        val isSpecCategoryTitle: Boolean = false,
        val specCategoryTitle: String = "",
        val specTitle: String = "",
        val specValue: String = "",
        var specHeight: Int = -1,
        var specTextColor: Int? = null,
        var specTextTitleColor: Int? = null,
        var isSpecTextTitleBold: Boolean = false,
        val isDarkMode: Boolean

    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
