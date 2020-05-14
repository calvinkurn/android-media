package com.tokopedia.discovery2.data.productcardcarousel


import com.google.gson.annotations.SerializedName

data class FreeOngkir(
        @SerializedName("img_url")
        val imgUrl: String? ,
        @SerializedName("is_active")
        val isActive: Boolean? = false
)