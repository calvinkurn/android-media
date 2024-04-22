package com.tokopedia.product.detail.data.model.variant_wishlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductVariantWishlistResponse(
    @SerializedName("productID")
    @Expose
    val productId: String = "",
    @SerializedName("isWishlist")
    @Expose
    val isWishlist: Boolean = false
)

fun List<ProductVariantWishlistResponse>.asUiModel() =
    this.associateBy(
        keySelector = { it.productId },
        valueTransform = {
            it.isWishlist
        }
    )
