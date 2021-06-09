package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

object MultipleProductCardMapper {

    fun map(notification: NotificationItemViewBean): List<MultipleProductCardViewBean> {
        val multiProductCards = arrayListOf<MultipleProductCardViewBean>()
        notification.products.forEachIndexed { index, product ->
            val multiProductCardItem = MultipleProductCardViewBean()
            multiProductCardItem.indexId = index
            multiProductCardItem.product = product
            multiProductCardItem.userInfo = notification.userInfo
            multiProductCardItem.templateKey = notification.templateKey
            multiProductCardItem.notificationId = notification.notificationId
            multiProductCards.add(multiProductCardItem)
        }
        return sortMultipleProductCardBasedOnStock(multiProductCards)
    }

    fun map(notification: MultipleProductCardViewBean): NotificationItemViewBean {
        return NotificationItemViewBean().apply {
            indexId = notification.indexId
            notificationId = notification.notificationId
            products = listOf(notification.product)
            templateKey = notification.templateKey
            userInfo = notification.userInfo
            title = notification.title
            body = notification.body
        }
    }

    private fun sortMultipleProductCardBasedOnStock(multiProductCards: ArrayList<MultipleProductCardViewBean>): List<MultipleProductCardViewBean> {
        val sortedMultiProductCards = arrayListOf<MultipleProductCardViewBean>()
        val emptyStockMultiProductCards = arrayListOf<MultipleProductCardViewBean>()
        multiProductCards.forEach { productCard ->
            if(productCard.product.stock > 0) {
                sortedMultiProductCards.add(productCard)
            } else {
                emptyStockMultiProductCards.add(productCard)
            }
        }
        sortedMultiProductCards.addAll(emptyStockMultiProductCards)
        return sortedMultiProductCards
    }

}