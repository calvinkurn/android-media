package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class BannerCatalogUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val imageUrl: String = "",
    val ratio: Ratio = Ratio.ONE_BY_ONE
) : BaseCatalogUiModel(
    idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor,
    darkMode
) {
    enum class Ratio(val ratioName: String) {
        ONE_BY_ONE("1:1"),
        TWO_BY_ONE("2:1"),
        THREE_BY_FOUR("3:4")
    }

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
