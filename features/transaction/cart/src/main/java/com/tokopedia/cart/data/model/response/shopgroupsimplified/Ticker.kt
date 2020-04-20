package com.tokopedia.cart.data.model.response


import com.google.gson.annotations.SerializedName

data class Ticker(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("page")
    val page: String
)