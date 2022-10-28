package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName

data class ErrorPinModel(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("message")
    val message: String = ""
)