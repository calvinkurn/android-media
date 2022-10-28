package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class ChatSearch(
    @SerializedName("contact")
    val contact: Contact = Contact()
)