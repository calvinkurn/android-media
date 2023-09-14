package com.tokopedia.catalog.ui.adapter

import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel

interface ProductListAdapterListener {
    fun onAtcButtonClicked(atcModel: CatalogProductAtcUiModel)

    fun onClickProductCard(item: CatalogProductItem, adapterPosition: Int)
}
