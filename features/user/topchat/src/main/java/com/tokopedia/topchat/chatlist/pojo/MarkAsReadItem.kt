package com.tokopedia.topchat.chatlist.pojo


import com.google.gson.annotations.SerializedName

data class MarkAsReadItem(
    @SerializedName("detailResponse")
    val detailResponse: String = "",
    @SerializedName("isSuccess")
    val isSuccess: Int = 0,
    @SerializedName("msgID")
    val msgID: Int = 0
)