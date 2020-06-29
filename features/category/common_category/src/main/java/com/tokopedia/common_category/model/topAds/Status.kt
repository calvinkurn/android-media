package com.tokopedia.common_category.model.topAds

import com.google.gson.annotations.SerializedName

data class Status(

        @field:SerializedName("error_code")
        val errorCode: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)