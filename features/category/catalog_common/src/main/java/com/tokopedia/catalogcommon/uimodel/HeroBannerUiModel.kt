package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class HeroBannerUiModel(
    override val idWidget: String,
    val content: String
) : BaseCatalogUiModel(idWidget) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
