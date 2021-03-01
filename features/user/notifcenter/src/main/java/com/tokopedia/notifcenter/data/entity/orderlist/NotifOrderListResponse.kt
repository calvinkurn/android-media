package com.tokopedia.notifcenter.data.entity.orderlist


import com.google.gson.annotations.SerializedName

data class NotifOrderListResponse(
    @SerializedName("notifcenter_notifOrderList")
    val notifcenterNotifOrderList: NotifcenterNotifOrderList = NotifcenterNotifOrderList()
)