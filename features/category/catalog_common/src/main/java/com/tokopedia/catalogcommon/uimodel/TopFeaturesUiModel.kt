package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class TopFeaturesUiModel(
    override val idWidget: String,
    var title: String = ""
): BaseCatalogUiModel(idWidget) {
    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
