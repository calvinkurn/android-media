package com.tokopedia.devicefingerprint.appauth.data

import com.google.gson.annotations.SerializedName

data class MutationSignDvcRequest(
        @SerializedName("version")
        val version: String = "1",
        @SerializedName("content")
        val content: String
)