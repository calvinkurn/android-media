package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.SerializedName

data class ChatGetGearList(
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,
    @SerializedName("list")
    val list: List<Any> = listOf()
)