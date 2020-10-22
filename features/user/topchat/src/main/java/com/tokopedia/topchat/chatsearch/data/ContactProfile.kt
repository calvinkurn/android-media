package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class ContactProfile(
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("role")
    val role: String = ""
)