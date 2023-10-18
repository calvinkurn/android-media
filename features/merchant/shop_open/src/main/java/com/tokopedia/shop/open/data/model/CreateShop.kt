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
        val createdId : String = "",
        @SerializedName("cta")
        @Expose
        val cta : Cta = Cta()
){
    data class Cta(
            @SerializedName("title")
            @Expose
            val title : String = "",
            @SerializedName("url")
            @Expose
            val url : String = ""
    )
}
