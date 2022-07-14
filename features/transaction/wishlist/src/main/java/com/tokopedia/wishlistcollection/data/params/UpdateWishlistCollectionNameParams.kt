package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName

data class UpdateWishlistCollectionNameParams(
    @SerializedName("collectionID")
    var collectionId: String = "",

    @SerializedName("collectionName")
    var collectionName: String = ""
)
