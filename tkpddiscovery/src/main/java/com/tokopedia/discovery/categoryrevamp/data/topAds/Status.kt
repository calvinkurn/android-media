package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName

data class Status(

        @field:SerializedName("error_code")
        val errorCode: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)