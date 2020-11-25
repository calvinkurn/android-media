package com.tokopedia.shop.open.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateShop(
        @SerializedName("createShop")
        @Expose
        val createShop : CreateShopData = CreateShopData()
)

data class CreateShopData (
        @SerializedName("success")
        @Expose
        val success : Boolean = false,
        @SerializedName("message")
        @Expose
        val message : String = "",
        @SerializedName("createdId")
        @Expose
        val createdId : String = ""
)