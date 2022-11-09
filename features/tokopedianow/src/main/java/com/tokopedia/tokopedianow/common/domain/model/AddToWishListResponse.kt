package com.tokopedia.tokopedianow.common.domain.model

import com.google.gson.annotations.SerializedName

data class AddToWishListResponse(

    @SerializedName("wishlist_add_v2")
    val wishlistAdd: AddToWishlistData? = null
)

data class AddToWishlistButton(

	@SerializedName("action")
	val action: String? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("url")
	val url: String? = null
)

data class AddToWishlistData(

	@SerializedName("button")
	val button: AddToWishlistButton? = null,

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
