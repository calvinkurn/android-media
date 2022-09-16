package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class CollectionWishlistButtonsItem(
    @SerializedName("color")
    val color: String = "",

    @SerializedName("action")
    val action: String = "",

    @SerializedName("text")
    val text: String = "",

    @SerializedName("url")
    val url: String = ""
)
