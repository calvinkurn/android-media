package com.tokopedia.similarsearch.productitem

import com.tokopedia.similarsearch.getsimilarproducts.model.Product

internal interface SimilarProductItemListener {

    fun onItemClicked(similarProductItem: Product, adapterPosition: Int)

    fun onItemWishlistClicked(productId: String, isWishlisted: Boolean)
}