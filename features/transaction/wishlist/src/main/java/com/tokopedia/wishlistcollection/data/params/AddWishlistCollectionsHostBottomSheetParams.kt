package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName

data class AddWishlistCollectionsHostBottomSheetParams(
    @SerializedName("collection_id")
    var collectionId: String = "",

    @SerializedName("product_ids")
    var productIds: List<String> = emptyList(),

    @SerializedName("collection_name")
    var collectionName: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        paramCollectionId to collectionId,
        paramCollectionName to collectionName,
        paramProductIds to productIds
    )

    companion object {
        const val paramCollectionId = "collection_id"
        const val paramCollectionName = "collection_name"
        const val paramProductIds = "product_ids"
    }
}
