package com.tokopedia.buyerorder.detail.revamp.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import java.util.*
import javax.inject.Inject

/**
 * created by @bayazidnasir on 29/8/2022
 */

class OrderDetailsAnalytics @Inject constructor() {

    companion object{
        private object Event {
            const val EVENT_NAME = "clickPurchaseList"
            const val EVENT_DETAIL = "my purchase list detail - mp"
            const val EVENT_ACTION_DOWNLOAD_INVOICE = "click button download invoice"
            const val EVENT_ACTION_CLICK_SEE_INVOICE = "click lihat invoice"
            const val EVENT_ACTION_CLICK_COPY_BUTTON = "click copy button"
            const val EVENT_ACTION_CLICK_PRIMARY_BUTTON = "click primary button"
            const val EVENT_ACTION_CLICK_SECONDARY_BUTTON = "click secondary button"
            const val EVENT_TRANSACTION = "transaction"
            const val EVENT_CATEGORY = "digital-deals"
            const val EVENT_CATEGORY_BUY_AGAIN_DETAIL_DG = "my purchase list detail - dg"
            const val EVENT_CATEGORY_ORDER_DETAIL_PAGE = "digital - order detail page"
            const val EVENT_RECHARGE_BUSINESS_UNIT = "recharge"
        }

        private object Key {
            const val ID = "id"
            const val CATEGORY_DEALS = "deals"
            const val CATEGORY_EVENTS = "hiburan"
            const val NAME = "name"
            const val PRICE = "price"
            const val ACTION = "view purchase attempt"
            const val CURRENCY_CODE = "currencyCode"
            const val IDR = "IDR"
            const val QUANTITY = "quantity"
            const val PAYMENT_TYPE = "payment_type"
            const val PAYMENT_STATUS = "payment_status"
            const val REVENUE = "revenue"
            const val AFFILIATION = "affiliation"
            const val BRAND = "brand"
            const val VARIANT = "variant"
            const val SHIPPING = "shipping"
            const val CATEGORY = "category"
            const val ATTRIBUTION = "attribution"
            const val TAX = "tax"
            const val COUPON_CODE = "coupon"
            const val KEY_PRODUCTS = "products"
            const val KEY_PURCHASE = "purchase"
            const val KEY_ACTION_FIELD = "actionField"
            const val KEY_CATEGORY = "Category"
            const val NONE = "none"
            const val ZERO = "0"
            const val DEALS_SCREEN_NAME = "/digital/deals/thanks"
            const val EVENTS_SCREEN_NAME = "/digital/events/thanks"
            const val DEALS_SCREEN_NAME_OMP = "/digital/omp/order-detail"
            const val DEALS_SCREEN_NAME_OMS = "/digital/oms/order-detail"
            const val ORDER_DETAIL_SCREEN_NAME = "order-detail-digital"
            const val DIGITAL_EVENT = "digital - event"
            const val BUSINESS_UNIT = "businessUnit"
            const val CURRENT_SITE = "currentSite"
            const val BUSINESS_UNIT_DEALS = "travel & entertainment"
            const val CURRENT_SITE_DEALS = "tokopediadigitaldeals"
            const val USER_ID = "userId"
            const val IS_LOGIN_STATUS = "isLoggedInStatus"
        }

        private object Action {
            const val PRODUCT_CLICK = "productClick"
            const val CLICK_ON_WIDGET_RECOMMENDATION = "click on widget recommendation"
            const val PRODUCT_VIEW = "productView"
            const val IMPRESSION_ON_WIDGET_RECOMMENDATION = "impression on widget recommendation"
            const val CLICK_CHECKOUT = "clickCheckout"
            const val EVENT = "event"
            const val EVENT_CATEGORY = "eventCategory"
            const val EVENT_ACTION = "eventAction"
            const val EVENT_LABEL = "eventLabel"
            const val ECOMMERCE = "ecommerce"
            const val LIST = "list"
            const val POSITION = "position"
            const val ACTION_FIELD = "actionField"
            const val PRODUCTS = "products"
            const val CLICK = "click"
            const val IMPRESSIONS = "impressions"
            const val CURRENT_SITE = "tokopediadigital"
        }

        private const val ZERO_PRICE = 0
        private const val TYPE_DEALS = 1
        private const val CATEGORY_EVENTS_FORMAT = "%s - %s"
    }

    fun sendDownloadEventData(eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
            Event.EVENT_NAME,
            Event.EVENT_DETAIL,
            Event.EVENT_ACTION_DOWNLOAD_INVOICE,
            eventLabel
        ))
    }

    fun sendThankYouEvent(
        entityProductId: Int,
        entityProductName: String,
        totalTicketPrice: Int,
        quantity: Int,
        brandName: String,
        orderId: String,
        categoryType: Int,
        paymentType: String,
        paymentStatus: String,
    ) {

        fun getProductType(): String {
            return if (categoryType == TYPE_DEALS) Key.CATEGORY_DEALS
            else Key.CATEGORY_EVENTS
        }

        val products = mapOf<String, Any>(
            Key.ID to entityProductId,
            Key.NAME to entityProductName,
            Key.PRICE to totalTicketPrice,
            Key.KEY_CATEGORY to getProductType(),
            Key.QUANTITY to quantity,
            Key.BRAND to brandName,
            Key.COUPON_CODE to Key.NONE,
            Key.VARIANT to Key.NONE
        )

        val actionField = mapOf<String, Any>(
            Key.ID to orderId,
            Key.REVENUE to totalTicketPrice,
            Key.AFFILIATION to brandName,
            Key.SHIPPING to Key.ZERO,
            Key.TAX to Key.NONE,
            Key.COUPON_CODE to Key.NONE
        )

        val ecommerce = mapOf<String, Any>(
            Key.CURRENCY_CODE to Key.IDR,
            Key.KEY_PRODUCTS to Collections.singletonList(products),
            Key.KEY_PURCHASE to mapOf(
                Key.KEY_ACTION_FIELD to actionField
            ),
        )

        fun getEventCategory(): String {
            return if (categoryType == TYPE_DEALS) Event.EVENT_CATEGORY
            else Key.DIGITAL_EVENT
        }

        fun getEventLabel(): String {
            return if (categoryType == TYPE_DEALS) brandName
            else String.format(CATEGORY_EVENTS_FORMAT, Key.CATEGORY_EVENTS, entityProductName)
        }

        val map = mapOf(
            Action.EVENT to Event.EVENT_TRANSACTION,
            Action.EVENT_CATEGORY to getEventCategory(),
            Action.EVENT_LABEL to getEventLabel(),
            Action.EVENT_ACTION to Key.ACTION,
            Key.CURRENT_SITE to Action.CURRENT_SITE,
            Key.PAYMENT_STATUS to paymentStatus,
            Key.PAYMENT_TYPE to paymentType,
            Action.ECOMMERCE to ecommerce
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)

        fun getScreenType(): String {
            return if (categoryType == TYPE_DEALS) Key.DEALS_SCREEN_NAME
            else Key.EVENTS_SCREEN_NAME
        }

        TrackApp.getInstance().gtm.sendScreenAuthenticated(getScreenType())
    }

    fun sendOpenScreenDeals(isOMP: Boolean) {
        fun getScreenName(): String {
            return if (isOMP) Key.DEALS_SCREEN_NAME_OMP
            else Key.DEALS_SCREEN_NAME_OMS
        }

        val map = mapOf(
            Key.BUSINESS_UNIT to Key.BUSINESS_UNIT_DEALS,
            Key.CURRENT_SITE to Key.CURRENT_SITE_DEALS,
        )

        TrackApp.getInstance().gtm.sendScreenAuthenticated(getScreenName(), map)
    }

    fun eventWidgetListView(contentItemTab: RecommendationItem, position: Int) {
        val eventCategory = "my purchase list - ${contentItemTab.title}"
        val eventLabel = "${contentItemTab.trackingData?.itemType} - ${contentItemTab.trackingData?.categoryName} - ${1 + position}"
        val ecommerce = DataLayer.mapOf(
            Key.CURRENCY_CODE, Key.IDR,
            Action.IMPRESSIONS, DataLayer.listOf(DataLayer.mapOf(
                Key.NAME, contentItemTab.subtitle,
                Key.ID, contentItemTab.trackingData?.productID,
                Key.PRICE, ZERO_PRICE,
                Action.LIST, contentItemTab.title,
                Action.POSITION, (position + 1),
                Key.CATEGORY, contentItemTab.trackingData?.categoryName,
                Key.VARIANT, contentItemTab.trackingData?.itemType
            ))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
            Action.EVENT, Action.PRODUCT_VIEW,
            Action.EVENT_CATEGORY, eventCategory,
            Action.EVENT_ACTION, Action.IMPRESSION_ON_WIDGET_RECOMMENDATION,
            Action.EVENT_LABEL, eventLabel,
            Action.ECOMMERCE, ecommerce
        ))
    }

    fun eventWidgetClick(contentItemTab: RecommendationItem, position: Int) {
        val eventLabel = "${contentItemTab.trackingData?.itemType} - ${contentItemTab.trackingData?.categoryName} - ${(1 + position)}"
        val products = DataLayer.mapOf(
            Key.NAME, contentItemTab.subtitle,
            Key.ID, contentItemTab.trackingData?.productID,
            Key.PRICE, ZERO_PRICE,
            Key.BRAND, Key.NONE,
            Key.CATEGORY, contentItemTab.trackingData?.categoryName,
            Key.VARIANT, contentItemTab.trackingData?.itemType,
            Action.LIST, contentItemTab.title,
            Action.POSITION, (position + 1),
            Key.ATTRIBUTION, Key.NONE
        )
        val actionField = DataLayer.mapOf(Action.LIST, contentItemTab.title)
        val clicks = DataLayer.mapOf(
            Action.ACTION_FIELD, actionField,
            Action.PRODUCTS, products
        )
        val ecommerce = DataLayer.mapOf(
            Action.CLICK, clicks
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
            Action.EVENT, Action.PRODUCT_CLICK,
            Action.EVENT_CATEGORY, Event.EVENT_CATEGORY_BUY_AGAIN_DETAIL_DG,
            Action.EVENT_ACTION, Action.CLICK_ON_WIDGET_RECOMMENDATION,
            Action.EVENT_LABEL, eventLabel,
            Action.ECOMMERCE, ecommerce,
        ))
    }

    fun sendOrderDetailImpression(userId: String) {
        val isLoggedInStatus = if (userId.isEmpty()) "false" else "true"

        TrackApp.getInstance().gtm.sendScreenAuthenticated(Key.ORDER_DETAIL_SCREEN_NAME, DataLayer.mapStringsOf(
            Key.BUSINESS_UNIT, Event.EVENT_RECHARGE_BUSINESS_UNIT,
            Key.CURRENT_SITE, Action.CURRENT_SITE,
            Key.USER_ID, userId,
            Key.IS_LOGIN_STATUS, isLoggedInStatus
        ))
    }

    fun sendInvoiceCLickEvent(categoryName: String, productName: String, userId: String) {
        val eventLabel = String.format(CATEGORY_EVENTS_FORMAT, categoryName, productName)
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
            Action.EVENT, Action.CLICK_CHECKOUT,
            Action.EVENT_CATEGORY, Event.EVENT_CATEGORY_ORDER_DETAIL_PAGE,
            Action.EVENT_ACTION, Event.EVENT_ACTION_CLICK_SEE_INVOICE,
            Action.EVENT_LABEL, eventLabel,
            Key.BUSINESS_UNIT, Event.EVENT_RECHARGE_BUSINESS_UNIT,
            Key.CURRENT_SITE, Action.CURRENT_SITE,
            Key.USER_ID, userId
        ))
    }

    fun sendCopyButtonClickEvent(categoryName: String, productName: String, userId: String) {
        val eventLabel = String.format(CATEGORY_EVENTS_FORMAT, categoryName, productName)
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
            Action.EVENT, Action.CLICK_CHECKOUT,
            Action.EVENT_CATEGORY, Event.EVENT_CATEGORY_ORDER_DETAIL_PAGE,
            Action.EVENT_ACTION, Event.EVENT_ACTION_CLICK_COPY_BUTTON,
            Action.EVENT_LABEL, eventLabel,
            Key.BUSINESS_UNIT, Event.EVENT_RECHARGE_BUSINESS_UNIT,
            Key.CURRENT_SITE, Action.CURRENT_SITE,
            Key.USER_ID, userId
        ))
    }

    fun sendActionButtonClickEvent(
        categoryName: String,
        productName: String,
        buttonId: String,
        buttonName: String,
        userId: String
    ) {
        val eventAction = if (buttonId == ActionButton.PRIMARY_BUTTON) {
            Event.EVENT_ACTION_CLICK_PRIMARY_BUTTON
        } else Event.EVENT_ACTION_CLICK_SECONDARY_BUTTON

        val eventLabel = String.format(
            "%s - %s - %s",
            categoryName, productName, buttonName
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
            Action.EVENT, Action.CLICK_CHECKOUT,
            Action.EVENT_CATEGORY, Event.EVENT_CATEGORY_ORDER_DETAIL_PAGE,
            Action.EVENT_ACTION, eventAction,
            Action.EVENT_LABEL, eventLabel,
            Key.BUSINESS_UNIT, Event.EVENT_RECHARGE_BUSINESS_UNIT,
            Key.CURRENT_SITE, Action.CURRENT_SITE,
            Key.USER_ID, userId
        ))
    }

}