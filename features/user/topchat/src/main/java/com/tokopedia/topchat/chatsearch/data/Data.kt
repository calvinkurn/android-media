package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("contact")
    val contact: ContactX = ContactX(),
    @SerializedName("createBy")
    val createBy: Int = 0,
    @SerializedName("createTimeStr")
    val createTimeStr: String = "",
    @SerializedName("lastMessage")
    val lastMessage: String = "",
    @SerializedName("msgId")
    val msgId: Int = 0,
    @SerializedName("oppositeId")
    val oppositeId: Int = 0,
    @SerializedName("oppositeType")
    val oppositeType: Int = 0
)