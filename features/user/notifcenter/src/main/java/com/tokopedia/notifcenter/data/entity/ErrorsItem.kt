package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorsItem(
        @SerializedName("path")
        @Expose
        val path: List<String> = ArrayList(),
        @SerializedName("message")
        @Expose
        val message: String = ""
)