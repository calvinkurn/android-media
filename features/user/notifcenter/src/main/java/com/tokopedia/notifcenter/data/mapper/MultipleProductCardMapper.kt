package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

object MultipleProductCardMapper {

    fun map(notification: NotificationItemViewBean): List<MultipleProductCardViewBean> {
        val multiProductCards = arrayListOf<MultipleProductCardViewBean>()
        notification.products.forEach {
            val multiProductCardItem = MultipleProductCardViewBean()
            multiProductCardItem.product = it
            multiProductCardItem.userInfo = notification.userInfo
            multiProductCards.add(multiProductCardItem)
        }
        return multiProductCards
    }

}