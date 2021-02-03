package com.tokopedia.notifcenter.analytics

import com.tokopedia.abstraction.processor.beta.ProductListClickBundler
import com.tokopedia.abstraction.processor.beta.ProductListClickProduct
import com.tokopedia.abstraction.processor.beta.ProductListImpressionBundler
import com.tokopedia.abstraction.processor.beta.ProductListImpressionProduct
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.inboxcommon.analytic.InboxAnalyticCommon
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class NotificationAnalytic @Inject constructor(
        private val userSession: UserSessionInterface
) {
    private val CURRENCY_IDR = "IDR"
    private val KEY_EVENT_LABEL = "eventLabel"

    companion object {
        const val LIST_NOTIFCENTER = "/notifcenter"
    }

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

    class EventAction private constructor() {
        companion object {
            const val VIEW_PRODUCT = "view on product thumbnail"
            const val CLICK_PRODUCT = "click on product thumbnail"
            const val CLICK_PRODUCT_ATC = "click on atc button"
            const val CLICK_PRODUCT_BUY = "click on buy button"
            const val CLICK_REMIND_ME = "click on ingatkan saya"
            const val CLICK_SEE_MORE_NEW = "click on see more at new section"
            const val CLICK_SEE_MORE_EARLIER = "click on see more at earlier section"
            const val CLICK_NOTIF_SETTINGS = "click on notif settings"
            const val CLICK_FILTER_REQUEST = "click on filter request"
            const val CLICK_WIDGET_CTA = "click cta on notif"
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
                    price = product.price,
                    currency = CURRENCY_IDR,
                    index = position.toLong(),
                    list = LIST_NOTIFCENTER,
                    dimension40 = null,
                    dimension87 = null,
                    dimension88 = null,
                    stringCollection = mapOf(
                            "dimension79" to product.shop.id.toString()
                    )
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
                        price = product.price,
                        currency = CURRENCY_IDR,
                        index = position.toLong(),
                        keyDimension40 = "",
                        stringCollection = hashMapOf(
                                Product.KEY_LIST to LIST_NOTIFCENTER,
                                "dimension79" to product.shop.id.toString(),
                                "attribution" to ""
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

    fun trackFilterClick(
            filterType: Long,
            filterName: String,
            @RoleType
            role: Int?
    ) {
        if (filterType == NotifcenterDetailUseCase.FILTER_NONE || role == null) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                InboxAnalyticCommon.createGeneralEvent(
                        event = Event.CLICK_NOTIF_CENTER,
                        eventCategory = EventCategory.NOTIFCENTER,
                        eventAction = EventAction.CLICK_FILTER_REQUEST,
                        eventLabel = filterName,
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId,
                        userRole = getRoleString(role)
                )
        )
    }

    fun trackClickCtaWidget(
            element: NotificationUiModel,
            @RoleType
            role: Int?
    ) {
        if (role == null) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                InboxAnalyticCommon.createGeneralEvent(
                        event = Event.CLICK_NOTIF_CENTER,
                        eventCategory = EventCategory.NOTIFCENTER,
                        eventAction = "${EventAction.CLICK_WIDGET_CTA} - ${element.widgetCtaText}",
                        eventLabel = getEventLabel(element),
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId,
                        userRole = getRoleString(role)
                )
        )
    }

    private fun getEventLabel(notification: NotificationUiModel): String {
        return "notif_list - ${notification.templateKey} - ${notification.notifId}"
    }

    private fun getRoleString(@RoleType role: Int): String {
        return when (role) {
            RoleType.BUYER -> "buyer"
            RoleType.SELLER -> "seller"
            else -> ""
        }
    }
}