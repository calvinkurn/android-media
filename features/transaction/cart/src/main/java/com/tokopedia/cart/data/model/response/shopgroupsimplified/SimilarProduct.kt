package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SimilarProduct(
        @field:Expose
        @field:SerializedName("text")
        val text: String = "",
        @field:Expose
        @field:SerializedName("url")
        val url: String = ""
)