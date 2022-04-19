package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class ResultStatusEntity (
    @SerializedName("code")
    var code:Int = 0,

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("message")
    var messages: List<String>? = null
)