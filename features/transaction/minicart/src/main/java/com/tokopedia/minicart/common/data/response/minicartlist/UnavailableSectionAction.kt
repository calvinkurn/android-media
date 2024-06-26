package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class UnavailableSectionAction(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = ""
)
