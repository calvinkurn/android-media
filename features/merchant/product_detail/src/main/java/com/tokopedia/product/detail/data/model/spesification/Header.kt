package com.tokopedia.product.detail.data.model.spesification


import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("process_time")
    val processTime: String = "",
    @SerializedName("status")
    val status: String = ""
)