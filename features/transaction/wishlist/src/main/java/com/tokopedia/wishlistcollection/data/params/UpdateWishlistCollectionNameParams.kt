package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName

data class UpdateWishlistCollectionNameParams(
    @SerializedName("collectionID")
    var collectionId: String = "",

    @SerializedName("collectionName")
    var collectionName: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        paramCollectionId to collectionId,
        paramCollectionName to collectionName
    )

    companion object {
        private const val paramCollectionId = "collectionID"
        private const val paramCollectionName = "collectionName"
    }
}
