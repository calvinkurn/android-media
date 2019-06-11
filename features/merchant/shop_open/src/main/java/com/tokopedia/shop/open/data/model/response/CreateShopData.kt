package com.tokopedia.shop.open.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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