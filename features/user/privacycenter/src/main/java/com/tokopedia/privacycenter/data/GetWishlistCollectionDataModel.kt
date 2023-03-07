package com.tokopedia.privacycenter.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class WishlistCollectionsParams(
    @SerializedName("params")
    var param: GetWishlistCollectionsParams = GetWishlistCollectionsParams()
) : GqlParam

data class GetWishlistCollectionsParams(
    @SerializedName("collection_access")
    val collectionAccess: Int = 0
)

data class GetWishlistCollectionResponse(
    @SerializedName("get_wishlist_collections")
    val getWishlistCollections: WishlistCollectionResponse = WishlistCollectionResponse()
)

data class WishlistCollectionResponse(
    @SerializedName("error_message")
    val errorMessage: List<String> = listOf(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: WishlistDataModel = WishlistDataModel()
)

data class WishlistDataModel(
    @SerializedName("collections")
    val collections: List<WishlistCollectionsDataModel> = listOf(),
    @SerializedName("empty_wishlist_image_url")
    val emptyWishlistImageUrl: String = "",
    @SerializedName("is_empty_state")
    val isEmptyState: Boolean = false,
    @SerializedName("total_collection")
    val totalCollection: Int = 0
)

data class WishlistCollectionsDataModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("access")
    val access: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("total_item")
    val totalItem: Int = 0,
    @SerializedName("item_text")
    val itemText: String = ""
)
