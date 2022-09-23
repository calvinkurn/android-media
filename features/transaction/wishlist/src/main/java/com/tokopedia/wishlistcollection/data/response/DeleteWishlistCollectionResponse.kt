package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class DeleteWishlistCollectionResponse(
	@SerializedName("delete_wishlist_collection")
	val deleteWishlistCollection: DeleteWishlistCollection
) {
	data class DeleteWishlistCollection(

		@SerializedName("error_message")
		val errorMessage: List<String> = emptyList(),

		@SerializedName("data")
		val data: DataDelete = DataDelete(),

		@SerializedName("status")
		val status: String = ""
	) {
		data class DataDelete(
			@SerializedName("success")
			val success: Boolean = false,

			@SerializedName("message")
			val message: String = ""
		)
	}
}
