package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class TrustMakerUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    val items: List<ItemTrustMakerUiModel>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
    data class ItemTrustMakerUiModel(
        val id: String,
        val icon: String,
        val title: String,
        val subTitle: String,
        val textColorTitle: Int,
        val textColorSubTitle: Int
    )
}
