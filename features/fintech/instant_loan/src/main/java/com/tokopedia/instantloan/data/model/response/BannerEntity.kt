package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class BannerEntity(
        @field:SerializedName("image")
        var image: String?,
        @field:SerializedName("link")
        var link: String?)
