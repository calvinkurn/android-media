package com.tokopedia.discovery.categoryrevamp.view.interfaces

import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem

interface ProductCardListener {

    fun onItemClicked(item: ProductsItem, adapterPosition: Int)

    fun onLongClick(item: ProductsItem, adapterPosition: Int)

    fun onWishlistButtonClicked(productItem: ProductsItem, position: Int)

    fun onProductImpressed(item: ProductsItem, adapterPosition: Int)
}