package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class StickyNavigationUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? = null,
    val content: List<StickyNavigationItemData>
): BaseCatalogUiModel(idWidget, widgetType, widgetName) {

    data class StickyNavigationItemData(
        val title: String
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
