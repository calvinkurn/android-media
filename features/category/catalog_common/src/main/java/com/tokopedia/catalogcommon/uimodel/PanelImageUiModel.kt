package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class PanelImageUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? = null,
    override val widgetTextColor: Int? = null,
    val content: List<PanelImageItemData>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

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
