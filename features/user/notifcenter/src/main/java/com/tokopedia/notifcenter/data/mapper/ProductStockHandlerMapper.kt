package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.notifcenter.data.entity.ProductData
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

    fun mapEliminateMultipleProduct(data: ProductStockHandler, originData: ProductData): NotificationItemViewBean {
        val notification = NotificationItemViewBean()
        data.pojo.list.forEach {
            notification.notificationId = it.notifId
            notification.templateKey = it.templateKey
            notification.title = it.title
            notification.body = it.shortDescription
            notification.bodyHtml = it.shortDescriptionHtml
            notification.isShowBottomSheet = it.isShowBottomSheet
            notification.typeBottomSheet = it.typeBottomSheet
            notification.products = removeUnusedProductData(it.productData, originData)

        }
        return notification
    }

    private fun removeUnusedProductData(stockHandlerData: List<ProductData>, originData: ProductData): List<ProductData> {
        val result = arrayListOf<ProductData>()
        stockHandlerData.forEach {
            if(it.productId == originData.productId
                    && it.name == originData.name
                    && it.stock == originData.stock) {
                result.add(it)
                return@forEach
            }
        }
        return result
    }

}