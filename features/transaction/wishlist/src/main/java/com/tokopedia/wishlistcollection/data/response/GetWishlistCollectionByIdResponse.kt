package com.tokopedia.wishlistcollection.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionByIdResponse(

    @SuppressLint("Invalid Data Type")
    @SerializedName("get_wishlist_collection_by_id")
	val getWishlistCollectionById: GetWishlistCollectionById
) {
    data class GetWishlistCollectionById(

        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("data")
        val data: Data = Data(),

        @SerializedName("status")
        val status: String = ""
    ) {
        data class Data(

            @SerializedName("ticker")
            val ticker: Ticker = Ticker(),

            @SerializedName("access_options")
            val accessOptions: List<AccessOptionsItem> = emptyList(),

            @SerializedName("collection")
            val collection: Collection = Collection()
        ) {
            data class Ticker(

                @SerializedName("title")
                val title: String = "",

                @SerializedName("descriptions")
                val descriptions: List<String> = emptyList()
            )

            data class AccessOptionsItem(

                @SerializedName("name")
                val name: String,

                @SerializedName("description")
                val description: String,

                @SuppressLint("Invalid Data Type")
                @SerializedName("id")
                val id: Int
            )

            data class Collection(

                @SerializedName("access")
                val access: Int = 0,

                @SerializedName("name")
                val name: String = "",

                @SerializedName("id")
                val id: String = ""
            )
        }
    }
}
