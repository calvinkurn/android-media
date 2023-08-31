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
        val title: String
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        fun dummyNavigation() = StickyNavigationUiModel(
            "", "", "", "#FFFFFF".stringHexColorParseToInt(),
            content = listOf(
                StickyNavigationItemData("Sorotan"),
                StickyNavigationItemData("Informasi Detail"),
                StickyNavigationItemData("Ulasan Pembeli")
            )
        )
    }

}
