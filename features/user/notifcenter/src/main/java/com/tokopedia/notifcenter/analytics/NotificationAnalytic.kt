package com.tokopedia.notifcenter.analytics

import com.tokopedia.abstraction.processor.beta.ProductListClickBundler
import com.tokopedia.abstraction.processor.beta.ProductListClickProduct
import com.tokopedia.abstraction.processor.beta.ProductListImpressionBundler
import com.tokopedia.abstraction.processor.beta.ProductListImpressionProduct
import com.tokopedia.inboxcommon.analytic.InboxAnalyticCommon
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class NotificationAnalytic @Inject constructor() {

    private val LIST_NOTIFCENTER = "/notifcenter"
    private val CURRENCY_IDR = "IDR"
    private val KEY_EVENT_LABEL = "eventLabel"

    private class Event private constructor() {
        companion object {
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"
        }
    }

    private class EventCategory private constructor() {
        companion object {
            const val NOTIFCENTER = "notif center"
        }
    }

    private class EventAction private constructor() {
        companion object {
            const val VIEW_PRODUCT = "view on product thumbnail"
            const val CLICK_PRODUCT = "click on product thumbnail"
        }
    }

    private class BusinessUnit private constructor() {
        companion object {
            const val COMMUNICATION = "communication"
        }
    }

    private class CurrentSite private constructor() {
        companion object {
            const val MARKETPLACE = "tokopediamarketplace"
        }
    }

    private class Product private constructor() {
        companion object {
            const val KEY_LIST = "list"
        }
    }

    fun trackProductImpression(
            notification: NotificationUiModel,
            product: ProductData,
            position: Int
    ) {
        product.hashCode()
        val products: List<ProductListImpressionProduct> = List(1) {
            ProductListImpressionProduct(
                    id = product.productId.toString(),
                    name = product.name,
                    brand = null,
                    category = "",
                    variant = "",
                    price = product.price.toDouble(),
                    currency = CURRENCY_IDR,
                    index = position.toLong(),
                    list = LIST_NOTIFCENTER,
                    dimension40 = null,
                    dimension87 = null,
                    dimension88 = null,
                    stringCollection = emptyMap()
            )
        }

        val bundle = ProductListImpressionBundler.getBundle(
                null,
                products,
                CurrentSite.MARKETPLACE,
                Event.PRODUCT_VIEW,
                EventCategory.NOTIFCENTER,
                EventAction.VIEW_PRODUCT,
                BusinessUnit.COMMUNICATION,
                null,
                InboxAnalyticCommon.createGeneralEvent(
                        eventLabel = getEventLabel(notification)
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                ProductListImpressionBundler.KEY, bundle
        )
    }

    fun trackProductClick(
            notification: NotificationUiModel,
            product: ProductData,
            position: Int
    ) {
        product.hashCode()
        val products: ArrayList<ProductListClickProduct> = arrayListOf(
                ProductListClickProduct(
                        id = product.productId.toString(),
                        name = product.name,
                        brand = null,
                        category = "",
                        variant = "",
                        price = product.price.toDouble(),
                        currency = CURRENCY_IDR,
                        index = position.toLong(),
                        keyDimension40 = "",
                        stringCollection = hashMapOf(
                                Product.KEY_LIST to LIST_NOTIFCENTER
                        )
                )
        )

        val bundle = ProductListClickBundler.getBundle(
                LIST_NOTIFCENTER,
                products,
                CurrentSite.MARKETPLACE,
                Event.PRODUCT_CLICK,
                EventCategory.NOTIFCENTER,
                EventAction.CLICK_PRODUCT,
                BusinessUnit.COMMUNICATION,
                null,
                hashMapOf(
                        KEY_EVENT_LABEL to getEventLabel(notification)
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                ProductListImpressionBundler.KEY, bundle
        )
    }

    private fun getEventLabel(notification: NotificationUiModel): String {
        return "notif_list - ${notification.templateKey} - ${notification.notifId}"
    }
}