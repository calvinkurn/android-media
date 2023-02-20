package com.tokopedia.privacycenter.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionByIdResponse(
    @SuppressLint("Invalid Data Type")
    @SerializedName("get_wishlist_collection_by_id")
    val getWishlistCollectionById: WishlistCollectionByIdResponse = WishlistCollectionByIdResponse()
)

data class WishlistCollectionByIdResponse(
    @SerializedName("error_message")
    val errorMessage: List<String> = listOf(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: WishlistBydIdDataModel = WishlistBydIdDataModel()
)

data class WishlistBydIdDataModel(
    @SerializedName("ticker")
    val ticker: WishlistByIdTickerDataModel = WishlistByIdTickerDataModel(),
    @SerializedName("collection")
    val collection: WishlistCollectionByIdDataModel = WishlistCollectionByIdDataModel()
)

data class WishlistByIdTickerDataModel(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("descriptions")
    val descriptions: List<String> = listOf()
)

data class WishlistCollectionByIdDataModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("access")
    var access: Int = 0
)
