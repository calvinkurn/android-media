package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class AddWishlistCollectionsHostBottomSheetParams(
    @SerializedName("collection_id")
    var collectionId: String = "",

    @SerializedName("product_ids")
    var productIds: List<String> = emptyList(),

    @SerializedName("collection_name")
    var collectionName: String = ""
) : GqlParam
