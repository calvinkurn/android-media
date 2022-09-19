package com.tokopedia.checkout.data.model.response.egold

import com.google.gson.annotations.SerializedName

data class EgoldHyperlinkText(
    @SerializedName("text")
    var text: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("is_show")
    var isShow: Boolean = false
)
