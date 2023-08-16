package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class PanelImageUiModel(
    override val idWidget: String,
    val content: List<PanelImageItemData>
): BaseCatalogUiModel(idWidget) {

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
