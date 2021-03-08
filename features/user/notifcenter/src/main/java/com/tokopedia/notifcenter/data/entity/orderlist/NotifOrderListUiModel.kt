package com.tokopedia.notifcenter.data.entity.orderlist


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory

data class NotifOrderListUiModel(
        @SerializedName("list")
        var list: List<Card> = listOf()
) : Visitable<NotificationTypeFactory> {
    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun update(orderList: NotifOrderListUiModel) {
        list = orderList.list
    }
}