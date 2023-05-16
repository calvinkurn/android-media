package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class UpdateWishlistCollectionNameParams(
    @SerializedName("collectionID")
    var collectionId: String = "",

    @SerializedName("collectionName")
    var collectionName: String = ""
) : GqlParam
