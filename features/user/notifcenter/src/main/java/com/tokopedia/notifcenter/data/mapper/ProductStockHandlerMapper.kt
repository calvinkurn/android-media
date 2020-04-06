package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.notifcenter.data.entity.ProductStockHandler
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

object ProductStockHandlerMapper {

    fun map(data: ProductStockHandler): NotificationItemViewBean {
        val notification = NotificationItemViewBean()
        data.pojo.list.forEach {
            notification.notificationId = it.notifId
            notification.templateKey = it.templateKey
            notification.title = it.title
            notification.body = it.shortDescription
            notification.bodyHtml = it.shortDescriptionHtml
            notification.isShowBottomSheet = it.isShowBottomSheet
            notification.typeBottomSheet = it.typeBottomSheet
            notification.products = it.productData
        }
        return notification
    }

}