package com.tokopedia.buyerorder.detail.revamp.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import java.util.Collections
import javax.inject.Inject

/**
 * created by @bayazidnasir on 29/8/2022
 */

class OrderDetailsAnalytics @Inject constructor() {

    companion object{
        private object Event {
            const val EVENT_TRANSACTION = "transaction"
            const val EVENT_CATEGORY = "digital-deals"
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
            const val EVENT = "event"
            const val EVENT_CATEGORY = "eventCategory"
            const val EVENT_ACTION = "eventAction"
            const val EVENT_LABEL = "eventLabel"
            const val ECOMMERCE = "ecommerce"
            const val CURRENT_SITE = "tokopediadigital"
        }

        private const val TYPE_DEALS = 1
        private const val CATEGORY_EVENTS_FORMAT = "%s - %s"
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

    fun sendOrderDetailImpression(userId: String) {
        val isLoggedInStatus = if (userId.isEmpty()) "false" else "true"

        TrackApp.getInstance().gtm.sendScreenAuthenticated(Key.ORDER_DETAIL_SCREEN_NAME, DataLayer.mapStringsOf(
            Key.BUSINESS_UNIT, Event.EVENT_RECHARGE_BUSINESS_UNIT,
            Key.CURRENT_SITE, Action.CURRENT_SITE,
            Key.USER_ID, userId,
            Key.IS_LOGIN_STATUS, isLoggedInStatus
        ))
    }

}