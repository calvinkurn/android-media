package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class ResponsePhoneData(

        @SerializedName("data")
        var data: PhoneDataEntity? = null,

        @SerializedName("code")
        var code: Int = 0,

        @SerializedName("latency")
        var latency: String? = null
)