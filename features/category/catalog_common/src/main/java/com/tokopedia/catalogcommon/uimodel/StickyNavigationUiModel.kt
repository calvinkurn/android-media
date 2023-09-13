package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt

data class StickyNavigationUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    val content: List<StickyNavigationItemData>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName) {

    data class StickyNavigationItemData(
        val title: String,
        val anchorTo: String
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
