package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

data class ProductImage(
        @SerializedName("image_src")
        val imageSrc: String = "",
        @SerializedName("image_src_200_square")
        val imageSrc200Square: String = "",
        @SerializedName("image_src_300")
        val imageSrc300: String = "",
        @SerializedName("image_src_square")
        val imageSrcSquare: String = ""
)
