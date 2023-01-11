package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorEntity(
    @Expose @SerializedName("message")
    var message: String = ""
)
