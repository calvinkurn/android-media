package com.tokopedia.catalog.ui.adapter

import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel

interface ProductListAdapterListener {
    fun onAtcButtonClicked(
        atcModel: CatalogProductAtcUiModel,
        element: CatalogProductItem,
        adapterPosition: Int
    )

    fun onClickProductCard(item: CatalogProductItem, adapterPosition: Int)

    fun onProductImpression(item: CatalogProductItem, position: Int)
}
