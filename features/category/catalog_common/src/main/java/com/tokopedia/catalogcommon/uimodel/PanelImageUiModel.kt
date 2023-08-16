package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class PanelImageUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    val backgroundColorWidget: Int? = null,
    val content: List<PanelImageItemData>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName) {

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
