package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class DummyUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    val content: String
) : BaseCatalogUiModel(idWidget, widgetType, widgetName) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
