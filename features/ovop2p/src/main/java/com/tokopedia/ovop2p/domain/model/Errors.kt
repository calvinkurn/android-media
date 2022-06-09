package com.tokopedia.ovop2p.domain.model

import com.google.gson.annotations.SerializedName

data class Errors(
        @SerializedName("title")
        var title: String? = "",
        @SerializedName("message")
        var message: String? = "")
