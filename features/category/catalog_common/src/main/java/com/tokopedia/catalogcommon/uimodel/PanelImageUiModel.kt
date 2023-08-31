package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class PanelImageUiModel(
    override var idWidget: String,
    override var widgetType: String,
    override var widgetName: String,
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val content: List<PanelImageItemData>
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

    data class PanelImageItemData(
        val imageUrl: String,
        val highlight: String,
        val title: String,
        val description: String
    )
}
