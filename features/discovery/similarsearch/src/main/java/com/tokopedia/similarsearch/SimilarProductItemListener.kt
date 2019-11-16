package com.tokopedia.similarsearch

internal interface SimilarProductItemListener {

    fun onItemClicked(similarProductItem: Product, adapterPosition: Int)

    fun onItemWishlistClicked(productId: String, isWishlisted: Boolean)
}