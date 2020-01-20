package com.tokopedia.createpost.data.pojo.productsuggestion.shop


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopProductItem(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("image_uri")
        @Expose
        val imageUri: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Int = 0,

        @SerializedName("uri")
        @Expose
        val uri: String = ""
)