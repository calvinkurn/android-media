package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InterestPickEntity(
        @SerializedName("data")
        @Expose
        val data: Data
)