package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class TopFeaturesUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val items: List<ItemTopFeatureUiModel>
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
    data class ItemTopFeatureUiModel(
        val id: String,
        val icon: String,
        val name: String,
        val backgroundColor: Int,
        val textColor: Int
    )
}
