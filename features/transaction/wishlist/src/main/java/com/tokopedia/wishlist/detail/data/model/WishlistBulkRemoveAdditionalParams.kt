package com.tokopedia.wishlist.detail.data.model

import com.google.gson.annotations.SerializedName

data class WishlistBulkRemoveAdditionalParams(
    @SerializedName("excluded_product_ids")
    var excludedProductIds: List<Long> = emptyList(),

    @SerializedName("total_overlimit_items")
    var totalOverlimitItems: Long = 0L
)
