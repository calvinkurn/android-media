package com.tokopedia.wishlistcollection.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionSharingDataResponse(
	@SerializedName("get_wishlist_collection_sharing_data")
	val getWishlistCollectionSharingData: GetWishlistCollectionSharingData
) {
    data class GetWishlistCollectionSharingData(

        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("data")
        val data: Data = Data(),

        @SerializedName("status")
        val status: String = ""
    ) {
        data class Data(

            @SerializedName("share_link")
            val shareLink: ShareLink = ShareLink(),

            @SerializedName("total_item")
            val totalItem: Long = 0L,

            @SerializedName("empty_wishlist_image_url")
            val emptyWishlistImageUrl: String = "",

            @SerializedName("collection")
            val collection: Collection = Collection(),

            @SerializedName("items")
            val items: List<ItemsItem> = emptyList()
        ) {
            data class ShareLink(

                @SerializedName("deeplink")
                val deeplink: String = "",

                @SerializedName("redirection_url")
                val redirectionUrl: String = ""
            )

            data class Collection(

                @SerializedName("owner")
                val owner: Owner = Owner(),

                @SerializedName("name")
                val name: String = "",

                @SerializedName("id")
                val id: Long = 0L,

                @SerializedName("access")
                val access: Long = 0L,
            ) {
                data class Owner(

                    @SerializedName("name")
                    val name: String = "",

                    @SerializedName("id")
                    val id: Long = 0L
                )
            }

            data class ItemsItem(

                @SerializedName("image_url")
                val imageUrl: String = "",

                @SuppressLint("Invalid Data Type")
                @SerializedName("price")
                val price: Long = 0L,

                @SerializedName("name")
                val name: String = "",

                @SerializedName("id")
                val id: String = "",

                @SerializedName("price_fmt")
                val priceFmt: String = ""
            )
        }
    }
}
