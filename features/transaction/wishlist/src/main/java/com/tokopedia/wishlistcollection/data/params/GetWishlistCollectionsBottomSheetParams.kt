package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetWishlistCollectionsBottomSheetParams(
    @SerializedName("productIds")
    var productIds: String = "",

    @SerializedName("source")
    var source: String = "wishlist"
) : GqlParam
