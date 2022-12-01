package com.tokopedia.home.beranda.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SectionContent(
    @SerializedName("TagAttributes")
    @Expose
    val tagAttributes: TagAttributes = TagAttributes(),
    @SerializedName("TextAttributes")
    @Expose
    val subscriptionsTextAttributes: SubscriptionsTextAttributes = SubscriptionsTextAttributes(),
    @SerializedName("Type")
    @Expose
    val type: String = ""
)