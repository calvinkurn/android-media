package com.tokopedia.topchat.chatroom.domain.pojo.product_bundling

import com.google.gson.annotations.SerializedName

data class CtaBundling (
    @SerializedName("cta_text")
    var ctaText: String? = "",
    @SerializedName("is_disabled")
    var isDisabled: Boolean? = false,
    @SerializedName("button_shown")
    var isButtonShown: Boolean? = false,
    @SerializedName("android_link")
    var buttonAndroidLink: String = ""
)