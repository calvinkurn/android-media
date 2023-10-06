package com.tokopedia.catalog.ui.adapter

import com.tokopedia.catalog.domain.model.CatalogProductItem

interface CatalogProductListAdapterFactory {
    fun type(uiModel: CatalogProductItem): Int
}
