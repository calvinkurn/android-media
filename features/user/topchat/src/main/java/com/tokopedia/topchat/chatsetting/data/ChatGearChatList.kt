package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.SerializedName

data class ChatGearChatList(
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,
    @SerializedName("listBuyer")
    val listBuyer: List<Buyer> = listOf(),
    @SerializedName("listSeller")
    val listSeller: List<Seller> = listOf(),
    @SerializedName("listUtils")
    val listUtils: List<Utils> = listOf()
)