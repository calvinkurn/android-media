package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class UpdateWishlistCollectionNameResponse(
	@SerializedName("update_wishlist_collection_name")
	val updateWishlistCollectionName: UpdateWishlistCollectionName = UpdateWishlistCollectionName()) {

		data class UpdateWishlistCollectionName(

			@SerializedName("error_message")
			val errorMessage: List<String> = emptyList(),

			@SerializedName("data")
			val data: DataUpdate = DataUpdate(),

			@SerializedName("status")
			val status: String = ""
		) {
			data class DataUpdate(
				@SerializedName("success")
				val success: Boolean = false,

				@SerializedName("message")
				val message: String = ""
			)
		}
}