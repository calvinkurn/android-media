package com.tokopedia.notifcenter.data.entity.orderlist


import com.google.gson.annotations.SerializedName

data class NotifOrderListResponse(
    @SerializedName("notifcenter_notifOrderList")
    val notifcenterNotifOrderList: NotifOrderListUiModel = NotifOrderListUiModel()
) {
    fun clearAllCounter() {
        notifcenterNotifOrderList.list.forEach {
            it.counter = "0"
        }
    }
}