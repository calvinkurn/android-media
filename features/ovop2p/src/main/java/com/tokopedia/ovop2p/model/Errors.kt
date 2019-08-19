package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

data class Errors(
        @SerializedName("title")
        var title: String? = "",
        @SerializedName("message")
        var message: String? = "")
