package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName

data class ImageProduct(

        @SerializedName("image_url")
        var imageUrl: String? = null,

        @SerializedName("image_click_url")
        var imageClickUrl: String? = null
)