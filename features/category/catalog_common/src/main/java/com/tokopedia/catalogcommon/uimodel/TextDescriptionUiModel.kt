package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class TextDescriptionUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? = null,
    override val widgetTextColor: Int? = null,
    val item: ItemTextDescriptionUiModel,
    val isDarkMode: Boolean
): BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

    data class ItemTextDescriptionUiModel(
        val highlight: String,
        val title: String,
        val description: String
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
