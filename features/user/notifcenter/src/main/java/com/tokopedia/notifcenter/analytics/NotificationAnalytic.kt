package com.tokopedia.notifcenter.analytics

import com.tokopedia.abstraction.processor.beta.*
import com.tokopedia.inboxcommon.analytic.InboxAnalyticCommon
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class NotificationAnalytic @Inject constructor(
        private val userSession: UserSessionInterface
) {

    private val LIST_NOTIFCENTER = "/notifcenter"
    private val CURRENCY_IDR = "IDR"
    private val KEY_EVENT_LABEL = "eventLabel"

    private class Event private constructor() {
        companion object {
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"
            const val ADD_TO_CART = "addToCart"
            const val CLICK_NOTIF_CENTER = "clickNotifCenter"
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
            const val CLICK_PRODUCT_ATC = "click on atc button"
            const val CLICK_PRODUCT_BUY = "click on buy button"
            const val CLICK_REMIND_ME = "click on ingatkan saya"
            const val CLICK_SEE_MORE_NEW = "click on see more at new section"
            const val CLICK_SEE_MORE_EARLIER = "click on see more at earlier section"
            const val CLICK_NOTIF_SETTINGS = "click on notif settings"
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
                EventCategory.NOTIFCENTER,
                EventAction.CLICK_PRODUCT,
                Event.PRODUCT_CLICK,
                CurrentSite.MARKETPLACE,
                BusinessUnit.COMMUNICATION,
                null,
                hashMapOf(
                        KEY_EVENT_LABEL to getEventLabel(notification)
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                ProductListClickBundler.KEY, bundle
        )
    }

    fun trackClickAtc(
            notification: NotificationUiModel,
            product: ProductData
    ) {
        val products: ArrayList<AddToCartProduct> = arrayListOf(
                AddToCartProduct(
                        id = product.productId.toString(),
                        name = product.name,
                        brand = null,
                        category = "",
                        variant = "",
                        price = product.price.toDouble(),
                        quantity = product.minOrder.toLong(),
                        dimension45 = "",
                        stringCollection = hashMapOf(
                                "dimension40" to LIST_NOTIFCENTER,
                                "category_id" to "",
                                "quantity" to product.minOrder.toString(),
                                "shop_id" to product.shop.id.toString(),
                                "shop_name" to product.shop.name,
                                "shop_type" to ""
                        )
                )
        )

        val bundle = AddToCartBundler.getBundle(
                products,
                Event.ADD_TO_CART,
                EventAction.CLICK_PRODUCT_ATC,
                CurrentSite.MARKETPLACE,
                EventCategory.NOTIFCENTER,
                BusinessUnit.COMMUNICATION,
                null,
                InboxAnalyticCommon.createGeneralEvent(
                        eventLabel = getEventLabel(notification)
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                AddToCartBundler.KEY, bundle
        )
    }

    fun trackClickBuy(
            notification: NotificationUiModel,
            product: ProductData
    ) {
        val products: ArrayList<AddToCartProduct> = arrayListOf(
                AddToCartProduct(
                        id = product.productId.toString(),
                        name = product.name,
                        brand = null,
                        category = "",
                        variant = "",
                        price = product.price.toDouble(),
                        quantity = product.minOrder.toLong(),
                        dimension45 = "",
                        stringCollection = hashMapOf(
                                "dimension40" to LIST_NOTIFCENTER,
                                "category_id" to "",
                                "quantity" to product.minOrder.toString(),
                                "shop_id" to product.shop.id.toString(),
                                "shop_name" to product.shop.name,
                                "shop_type" to ""
                        )
                )
        )

        val bundle = AddToCartBundler.getBundle(
                products,
                Event.ADD_TO_CART,
                EventAction.CLICK_PRODUCT_BUY,
                CurrentSite.MARKETPLACE,
                EventCategory.NOTIFCENTER,
                BusinessUnit.COMMUNICATION,
                null,
                InboxAnalyticCommon.createGeneralEvent(
                        eventLabel = getEventLabel(notification)
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                AddToCartBundler.KEY, bundle
        )
    }

    fun trackBumpReminder() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                InboxAnalyticCommon.createGeneralEvent(
                        event = Event.CLICK_NOTIF_CENTER,
                        eventCategory = EventCategory.NOTIFCENTER,
                        eventAction = EventAction.CLICK_REMIND_ME,
                        eventLabel = "ingatkan saya",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackDeleteReminder() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                InboxAnalyticCommon.createGeneralEvent(
                        event = Event.CLICK_NOTIF_CENTER,
                        eventCategory = EventCategory.NOTIFCENTER,
                        eventAction = EventAction.CLICK_REMIND_ME,
                        eventLabel = "hapus pengingat",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackLoadMoreNew() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                InboxAnalyticCommon.createGeneralEvent(
                        event = Event.CLICK_NOTIF_CENTER,
                        eventCategory = EventCategory.NOTIFCENTER,
                        eventAction = EventAction.CLICK_SEE_MORE_NEW,
                        eventLabel = "",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackLoadMoreEarlier() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                InboxAnalyticCommon.createGeneralEvent(
                        event = Event.CLICK_NOTIF_CENTER,
                        eventCategory = EventCategory.NOTIFCENTER,
                        eventAction = EventAction.CLICK_SEE_MORE_EARLIER,
                        eventLabel = "",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackClickSettingNotif() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                InboxAnalyticCommon.createGeneralEvent(
                        event = Event.CLICK_NOTIF_CENTER,
                        eventCategory = EventCategory.NOTIFCENTER,
                        eventAction = EventAction.CLICK_NOTIF_SETTINGS,
                        eventLabel = "",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    private fun getEventLabel(notification: NotificationUiModel): String {
        return "notif_list - ${notification.templateKey} - ${notification.notifId}"
    }
}