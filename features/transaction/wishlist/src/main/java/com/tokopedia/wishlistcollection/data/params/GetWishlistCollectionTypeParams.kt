package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetWishlistCollectionTypeParams(
    @SerializedName("collection_id")
    var collectionId: String = ""
) : GqlParam
