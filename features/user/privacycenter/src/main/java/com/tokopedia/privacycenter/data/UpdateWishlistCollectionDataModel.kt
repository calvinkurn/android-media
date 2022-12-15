package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class UpdateWishlistParam(
    @SerializedName("params")
    val params: UpdateWishlistCollectionParam = UpdateWishlistCollectionParam()
) : GqlParam

data class UpdateWishlistCollectionParam(
    @SerializedName("collection")
    val collection: WishlistCollectionByIdDataModel = WishlistCollectionByIdDataModel()
)

data class UpdateWishlistCollectionResponse(
    @SerializedName("update_wishlist_collection")
    val updateWishlistCollection: UpdateWishlistResponse = UpdateWishlistResponse()
)

data class UpdateWishlistResponse(
    @SerializedName("error_message")
    val errorMessage: List<String> = listOf(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: UpdateWishlistDataModel = UpdateWishlistDataModel()
)

data class UpdateWishlistDataModel(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = ""
)
