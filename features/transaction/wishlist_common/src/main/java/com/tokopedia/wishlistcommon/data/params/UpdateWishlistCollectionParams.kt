package com.tokopedia.wishlistcommon.data.params

import com.google.gson.annotations.SerializedName

data class UpdateWishlistCollectionParams(
    @SerializedName("id")
    var id: Long = 0L,

    @SerializedName("name")
    var name: String = "",

    @SerializedName("access")
    var access: Long = 0L
)
