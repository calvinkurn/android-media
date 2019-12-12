package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("data")
    val `data`: List<Data> = listOf(),
    @SerializedName("hasNext")
    val hasNext: Boolean = false
)