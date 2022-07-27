package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PRODUCT_IDs
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.SOURCE

data class GetWishlistCollectionsBottomSheetParams(
    @SerializedName("product_ids")
    var productIds: String = "",

    @SerializedName("source")
    var source: String = "wishlist"
) {
    fun toMap(): Map<String, Any> = mapOf(
        PRODUCT_IDs to productIds,
        SOURCE to source
    )
}
