package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class AddWishlistBulkParams(
    @SerializedName("product_ids")
    var listProductId: ArrayList<String> = arrayListOf(),

    @SerializedName("user_id")
    var userId: String = "",

    @SerializedName("source")
    var source: String = "collection",

    @SerializedName("collection_sharing")
    var collectionSharing: CollectionSharing = CollectionSharing()
) : GqlParam {
    data class CollectionSharing(
        @SerializedName("source_collection_id")
        var sourceCollectionId: String = ""
    )
}
