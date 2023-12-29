package com.tokopedia.videoTabComponent.domain.model.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class PlayPinnedProduct(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("price_fmt")
        val priceFmt: String = "",
        @SerializedName("discount")
        val discount: Int = 0,
        @SerializedName("quantity")
        val quantity: Int = 0,
        @SerializedName("order")
        val order: Int = 0,
        @SerializedName("app_link")
        val appLink: String = "",
        @SerializedName("image_url")
        val imageUrl: String = "",
)
