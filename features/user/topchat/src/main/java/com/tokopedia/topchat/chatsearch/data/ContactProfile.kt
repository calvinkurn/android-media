package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class ContactProfile(
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @SerializedName("role")
    val role: String = ""
)