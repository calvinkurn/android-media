package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class ProductImage(
        @SerializedName("image_src_100_square")
        val imageSrc100Square: String = "",
)