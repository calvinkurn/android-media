package com.tokopedia.tokopedianow.common.domain.model

import com.google.gson.annotations.SerializedName

data class RemoveFromWishListResponse(

    @SerializedName("wishlist_remove_v2")
    val wishlistRemove: RemoveFromWishlistData? = null
)

data class RemoveFromButton(

    @SerializedName("action")
    val action: String? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("url")
    val url: String? = null
)

data class RemoveFromWishlistData(

    @SerializedName("button")
    val button: RemoveFromButton? = null,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("toaster_color")
    val toasterColor: String? = null,

    @SerializedName("error_type")
    val errorType: Int? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("message")
    val message: String? = null
)
