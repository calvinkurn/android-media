package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName

data class UpdateWishlistCollectionParams(
    @SerializedName("id")
    var id: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("access")
    var access: String = "",
)
