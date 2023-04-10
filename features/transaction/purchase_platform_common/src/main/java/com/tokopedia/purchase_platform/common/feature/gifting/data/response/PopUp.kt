package com.tokopedia.purchase_platform.common.feature.gifting.data.response

import com.google.gson.annotations.SerializedName

data class PopUp(
    @SerializedName("button")
    val button: Button = Button(),
    @SerializedName("description")
    val description: String = "",
    @SerializedName("title")
    val title: String = ""
)
