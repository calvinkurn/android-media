package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionsBottomSheetParams(
    @SerializedName("product_ids")
    var productIds: String = "",

    @SerializedName("source")
    var source: String = "wishlist"
)
