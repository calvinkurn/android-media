package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class DoubleBannerCatalogUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val imageUrls: List<String> = listOf(),
) : BaseCatalogUiModel(
    idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor,
    darkMode
) {
    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
