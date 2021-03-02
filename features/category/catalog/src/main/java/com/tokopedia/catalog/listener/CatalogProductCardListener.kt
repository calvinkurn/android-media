package com.tokopedia.catalog.listener

import com.tokopedia.catalog.model.raw.CatalogProductItem


interface CatalogProductCardListener {

    fun onItemClicked(item: CatalogProductItem, adapterPosition: Int)

    fun onLongClick(item: CatalogProductItem, adapterPosition: Int)

    fun onWishlistButtonClicked(productItem: CatalogProductItem, position: Int)

    fun onProductImpressed(item: CatalogProductItem, adapterPosition: Int)

    fun onThreeDotsClicked(productItem: CatalogProductItem, position: Int) {

    }

    fun hasThreeDots() = false
}