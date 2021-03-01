package com.tokopedia.notifcenter.data.entity.orderlist


import com.google.gson.annotations.SerializedName

data class NotifcenterNotifOrderList(
    @SerializedName("list")
    val list: List<Card> = listOf()
)