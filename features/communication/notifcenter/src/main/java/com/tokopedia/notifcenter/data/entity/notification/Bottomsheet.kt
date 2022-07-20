package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Bottomsheet(
    @SerializedName("button_cta")
    val buttonCta: ButtonCta = ButtonCta(),
    @SerializedName("components")
    val components: List<Any> = listOf(),
    @SerializedName("title")
    val title: String = ""
)