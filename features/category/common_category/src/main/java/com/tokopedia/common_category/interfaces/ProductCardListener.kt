package com.tokopedia.common_category.interfaces

import com.tokopedia.common_category.model.productModel.ProductsItem


interface ProductCardListener {

    fun onItemClicked(item: ProductsItem, adapterPosition: Int)

    fun onLongClick(item: ProductsItem, adapterPosition: Int)

    fun onWishlistButtonClicked(productItem: ProductsItem, position: Int)

    fun onProductImpressed(item: ProductsItem, adapterPosition: Int)

    fun onThreeDotsClicked(productItem: ProductsItem, position: Int) {

    }

    fun hasThreeDots() = false
}