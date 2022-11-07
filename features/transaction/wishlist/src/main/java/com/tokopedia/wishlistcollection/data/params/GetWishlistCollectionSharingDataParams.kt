package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetWishlistCollectionSharingDataParams(
        @SerializedName("collectionID")
        var collectionId: Long = 0L): GqlParam
