package com.tokopedia.notifcenter.data.entity.orderlist

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationOrderListViewHolder

data class OrderWidgetUiModel(
    @SerializedName("counter_str")
    var counter: String = "0",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("link")
    val link: Link = Link(),
    @SerializedName("text")
    var text: String = ""
) : Visitable<NotificationOrderListViewHolder.OrderListTypeFactory> {
    fun hasCounter(): Boolean {
        return counter.isNotEmpty() && counter != "0"
    }

    override fun type(typeFactory: NotificationOrderListViewHolder.OrderListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
