package com.tokopedia.oldcatalog.listener

import com.tokopedia.oldcatalog.model.datamodel.CatalogForYouModel
import com.tokopedia.oldcatalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.oldcatalog.model.raw.CatalogProductItem


interface CatalogProductCardListener {

    fun onItemClicked(item: CatalogProductItem, adapterPosition: Int)

    fun onLongClick(item: CatalogProductItem, adapterPosition: Int)

    fun onWishlistButtonClicked(productItem: CatalogProductItem, position: Int)

    fun onProductImpressed(item: CatalogProductItem, adapterPosition: Int)

    fun onThreeDotsClicked(productItem: CatalogProductItem, position: Int) {

    }

    fun hasThreeDots() = false

    fun onCatalogForYouClick(adapterPosition: Int , catalogComparison: CatalogComparisonProductsResponse.CatalogComparisonList.CatalogComparison) {}

    fun onCatalogForYouImpressed(model : CatalogForYouModel, adapterPosition: Int){}

    fun setLastDetachedItemPosition(adapterPosition: Int) {}

    fun setLastAttachItemPosition(adapterPosition: Int) {}

}
