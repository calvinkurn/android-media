package com.tokopedia.unifyorderhistory.analytics

import android.app.Activity
import android.os.Bundle
import com.google.gson.JsonArray
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.unifyorderhistory.util.UohConsts.BUSINESS_UNIT_REPLACEE
import com.tokopedia.unifyorderhistory.util.UohConsts.RECOMMENDATION_LIST_TOPADS_TRACK
import com.tokopedia.unifyorderhistory.util.UohConsts.RECOMMENDATION_LIST_TRACK
import com.tokopedia.unifyorderhistory.analytics.data.model.ECommerceAdd
import com.tokopedia.unifyorderhistory.analytics.data.model.ECommerceAddRecommendation
import com.tokopedia.unifyorderhistory.analytics.data.model.ECommerceClick
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifyorderhistory.util.UohConsts
import java.util.*

/**
 * Created by fwidjaja on 2019-11-29.
 */
object UohAnalytics {
    private const val OPEN_SCREEN = "openScreen"
    private const val EVENT = "event"
    private const val SCREEN_NAME = "screenName"
    private const val ORDER_LIST_SCREEN_NAME = "order-list"
    private const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"
    private const val EVENT_CATEGORY = "eventCategory"
    private const val EVENT_ACTION = "eventAction"
    private const val EVENT_LABEL = "eventLabel"
    private const val CURRENT_SITE = "currentSite"
    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val USER_ID = "userId"
    private const val BUSINESS_UNIT = "businessUnit"
    private const val ECOMMERCE = "ecommerce"
    private const val ITEMS = "items"
    private const val CURRENCY_CODE = "currencyCode"
    private const val IDR = "IDR"
    private const val IMPRESSIONS = "impressions"
    private const val NAME = "name"
    private const val ITEM_NAME = "item_name"
    private const val ID = "id"
    private const val ITEM_ID = "item_id"
    private const val PRICE = "price"
    private const val BRAND = "brand"
    private const val ITEM_BRAND = "item_brand"
    private const val CATEGORY = "category"
    private const val ITEM_CATEGORY = "item_category"
    private const val VARIANT = "variant"
    private const val ITEM_VARIANT = "item_variant"
    private const val ORDER_DETAIL = "order-detail"
    private const val LIST = "list"
    private const val POSITION = "position"
    private const val INDEX = "index"
    private const val ADD = "add"
    private const val CLICK = "click"
    private const val ACTION_FIELD = "actionField"
    private const val PRODUCTS = "products"
    private const val ATTRIBUTION = "attribution"
    private const val QUANTITY = "quantity"
    private const val DIMENSION79 = "dimension79"
    private const val DIMENSION81 = "dimension81"
    private const val DIMENSION80 = "dimension80"
    private const val DIMENSION45 = "dimension45"
    private const val DIMENSION40 = "dimension40"
    private const val DIMENSION87 = "dimension87"
    private const val DIMENSION88 = "dimension88"
    private const val ORDER_MANAGEMENT = "order management"
    private const val CLICK_ORDER_LIST = "clickOrderList"
    private const val PRODUCT_VIEW = "productView"
    private const val ITEM_LIST = "item_list"
    private const val VIEW_ITEM_LIST = "view_item_list"
    private const val PRODUCT_CLICK = "productClick"
    private const val SELECT_CONTENT = "select_content"
    private const val ADD_TO_CART = "addToCart"
    private const val ADD_TO_CART_V5 = "add_to_cart"
    private const val VIEW_ORDER_CARD = "view order card {business_unit}"
    private const val EVENT_LABEL_RECOMMENDATION = "Rekomendasi Untuk Anda"
    private const val VIEW_RECOMMENDATION = "impression - product recommendation"
    private const val CLICK_RECOMMENDATION = "click - product recommendation"
    private const val CLICK_ATC_RECOMMENDATION = "click add to cart on my purchase list page"
    private const val CLICK_ORDER_CARD = "click order card {business_unit}"
    private const val CLICK_BELI_LAGI = "click beli lagi on order card marketplace"
    private const val ACTION_FIELD_CLICK_ECOMMERCE = "/order list - {business_unit}"
    private const val ORDER_LIST_EVENT_CATEGORY = "order list"
    private const val PURCHASE_LIST_EVENT_CATEGORY = "my purchase list - mp"
    private const val SUBMIT_SEARCH = "submit search from cari transaksi"
    private const val CLICK_DATE_FILTER_CHIPS = "click date filter chips"
    private const val CLICK_TERAPKAN_ON_DATE_FILTER_CHIPS = "click terapkan on date filter chips"
    private const val CLICK_STATUS_FILTER_CHIPS = "click status filter chips"
    private const val CLICK_TERAPKAN_ON_STATUS_FILTER_CHIPS = "click terapkan on status filter chips"
    private const val CLICK_CATEGORY_FILTER_CHIPS = "click category filter chips"
    private const val CLICK_TERAPKAN_ON_CATEGORY_FILTER_CHIPS = "click terapkan on category filter chips"
    private const val CLICK_X_CHIPS_TO_CLEAR_FILTER = "click x chips to clear filter"
    private const val CLICK_PRIMARY_BUTTON_ON_ORDER_CARD = "click primary button on order card "
    private const val CLICK_THREE_DOTS_MENU = "click three dot menu "
    private const val CLICK_SECONDARY_OPTION_ON_THREE_DOT_MENU = "click secondary option on three dot menu "
    private const val CLICK_MULAI_BELANJA_ON_EMPTY_ORDER_LIST = "click mulai belanja on empty order list"
    private const val CLICK_RESET_FILTER_ON_EMPTY_FILTER_RESULT = "click reset filter on empty filter result"
    private const val CLICK_LIHAT_BUTTON_ON_ATC_SUCCESS_TOASTER = "click lihat button on atc success toaster"
    private const val CLICK_SELESAI_ON_BOTTOM_SHEET_FINISH_TRANSACTION = "click selesai on bottom sheet finish transaction"
    private const val CLICK_AJUKAN_KOMPLAIN_ON_BOTTOM_SHEET_FINISH_TRANSACTION = "click ajukan komplain on bottom sheet finish transaction"
    private const val CLICK_KIRIM_ON_BOTTOM_SHEET_SEND_EMAIL = "click kirim on bottom sheet send email "
    private const val TRUE = "true"

    @JvmStatic
    fun sendScreenName(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    private fun sendEventCategoryAction(event: String, eventCategory: String,
                                        eventAction: String) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                             eventAction: String, eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }

    fun viewOrderListPage(trackingQueue: TrackingQueue, isLoggedInStatus: Boolean, userId: String) {
        val map = DataLayer.mapOf(
            EVENT, OPEN_SCREEN,
            SCREEN_NAME, ORDER_LIST_SCREEN_NAME,
            IS_LOGGED_IN_STATUS, isLoggedInStatus,
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            BUSINESS_UNIT, ORDER_MANAGEMENT,
            USER_ID, userId
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun submitSearch(keyword: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST,
                ORDER_LIST_EVENT_CATEGORY, SUBMIT_SEARCH, keyword)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickDateFilterChips(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST,
                ORDER_LIST_EVENT_CATEGORY, CLICK_DATE_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)

    }

    fun clickTerapkanOnDateFilterChips(dateOption: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST,
                ORDER_LIST_EVENT_CATEGORY, CLICK_TERAPKAN_ON_DATE_FILTER_CHIPS, dateOption)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickStatusFilterChips(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST,
                ORDER_LIST_EVENT_CATEGORY, CLICK_STATUS_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickTerapkanOnStatusFilterChips(statusOption: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_TERAPKAN_ON_STATUS_FILTER_CHIPS, statusOption)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickCategoryFilterChips(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_CATEGORY_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickTerapkanOnCategoryFilterChips(categoryOption: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_TERAPKAN_ON_CATEGORY_FILTER_CHIPS, categoryOption)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickXChipsToClearFilter(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST,
                ORDER_LIST_EVENT_CATEGORY, CLICK_X_CHIPS_TO_CLEAR_FILTER, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun viewOrderCard(trackingQueue: TrackingQueue, order: UohListOrder.Data.UohOrders.Order, userId: String, listProduct: JsonArray, position: String) {
        val map = DataLayer.mapOf(
            EVENT, PRODUCT_VIEW,
            EVENT_CATEGORY, ORDER_LIST_EVENT_CATEGORY,
            EVENT_ACTION, VIEW_ORDER_CARD.replace(BUSINESS_UNIT_REPLACEE, order.verticalCategory),
            EVENT_LABEL, "",
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            USER_ID, userId,
            BUSINESS_UNIT, ORDER_MANAGEMENT,
            ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS, convertOrderItemToDataImpressionObject(order, listProduct, position)
            )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun convertOrderItemToDataImpressionObject(order: UohListOrder.Data.UohOrders.Order, listProduct: JsonArray, position: String): List<Any>  {
        var eeProductId = ""
        var eeProductPrice = ""
        return DataLayer.listOf(
            order.metadata.products.forEachIndexed { index, product ->
                val itemName = product.title
                if (order.metadata.listProducts.isNotEmpty()) {
                    val objProduct = listProduct.get(index)?.asJsonObject
                    eeProductId = objProduct?.get(UohConsts.EE_PRODUCT_ID).toString()
                    eeProductPrice = objProduct?.get(UohConsts.EE_PRODUCT_PRICE).toString()
                }

                DataLayer.mapOf(
                    ITEM_NAME, itemName,
                    ITEM_ID, eeProductId,
                    PRICE, eeProductPrice,
                    ITEM_BRAND, "",
                    ITEM_VARIANT, "",
                    ITEM_CATEGORY, "",
                    INDEX, position
                )
            }
        )
    }

    fun clickOrderCard(verticalLabel: String, userId: String, arrayListProducts: ArrayList<ECommerceClick.Products>) {
        val arrayListBundleItems = arrayListOf<Bundle>()
        arrayListProducts.forEach { product ->
            val bundleProduct = Bundle().apply {
                putString(ITEM_NAME, product.name)
                putString(ITEM_ID, product.id)
                putString(PRICE, product.price)
                putString(ITEM_BRAND, "")
                putString(ITEM_CATEGORY, "")
                putString(ITEM_VARIANT, "")
                putString(INDEX, product.position)
            }
            arrayListBundleItems.add(bundleProduct)
        }

        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_CATEGORY, ORDER_LIST_EVENT_CATEGORY)
            putString(EVENT_ACTION, CLICK_ORDER_CARD.replace(BUSINESS_UNIT_REPLACEE, verticalLabel))
            putString(EVENT_LABEL, "")
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(BUSINESS_UNIT, ORDER_MANAGEMENT)
            putString(ITEM_LIST, ACTION_FIELD_CLICK_ECOMMERCE.replace(BUSINESS_UNIT_REPLACEE, verticalLabel))
            putParcelableArrayList(ITEMS, arrayListBundleItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    fun clickPrimaryButtonOnOrderCard(verticalLabel: String, primaryButton: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_PRIMARY_BUTTON_ON_ORDER_CARD + verticalLabel, primaryButton)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickThreeDotsMenu(verticalLabel: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_THREE_DOTS_MENU + verticalLabel, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickSecondaryOptionOnThreeDotsMenu(verticalLabel: String, secondaryOption: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_SECONDARY_OPTION_ON_THREE_DOT_MENU + verticalLabel, secondaryOption)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickMulaiBelanjaOnEmptyOrderList(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_MULAI_BELANJA_ON_EMPTY_ORDER_LIST, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickResetFilterOnEmptyFilterResult(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_RESET_FILTER_ON_EMPTY_FILTER_RESULT, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickLihatButtonOnAtcSuccessToaster(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_LIHAT_BUTTON_ON_ATC_SUCCESS_TOASTER, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickBeliLagiOnOrderCardMP(screenName: String, userId: String,
                                   arrayListProducts: ArrayList<ECommerceAdd.Add.Products>,
                                   verticalLabel: String, cartId: String) {
        val arrayListBundleItems = arrayListOf<Bundle>()
        arrayListProducts.forEach { product ->
            val bundleProduct = Bundle().apply {
                putString(ITEM_NAME, product.name)
                putString(ITEM_ID, product.id)
                putString(PRICE, product.price)
                putString(ITEM_BRAND, "")
                putString(ITEM_CATEGORY, "")
                putString(ITEM_VARIANT, "")
                putString(QUANTITY, product.quantity)
                putString(DIMENSION79, product.dimension79)
                putString(DIMENSION81, "")
                putString(DIMENSION80, "")
                putString(DIMENSION45, cartId)
                putString(DIMENSION40, ACTION_FIELD_CLICK_ECOMMERCE.replace(BUSINESS_UNIT_REPLACEE, verticalLabel))
            }
            arrayListBundleItems.add(bundleProduct)
        }

        val bundle = Bundle().apply {
            putString(EVENT, ADD_TO_CART_V5)
            putString(EVENT_CATEGORY, ORDER_LIST_EVENT_CATEGORY)
            putString(EVENT_ACTION, CLICK_BELI_LAGI)
            putString(EVENT_LABEL, "success")
            putString(SCREEN_NAME, screenName)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(BUSINESS_UNIT, ORDER_MANAGEMENT)
            putParcelableArrayList(ITEMS, arrayListBundleItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART_V5, bundle)
    }

    fun clickSelesaiOnBottomSheetFinishTransaction(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_SELESAI_ON_BOTTOM_SHEET_FINISH_TRANSACTION, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickAjukanKomplainOnBottomSheetFinishTransaction(userId: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_AJUKAN_KOMPLAIN_ON_BOTTOM_SHEET_FINISH_TRANSACTION, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickKirimOnBottomSheetSendEmail(userId: String, verticalCategory: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_ORDER_LIST, ORDER_LIST_EVENT_CATEGORY,
                CLICK_KIRIM_ON_BOTTOM_SHEET_SEND_EMAIL + verticalCategory, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun productViewRecommendation(trackingQueue: TrackingQueue, userId: String, recommendationItem: RecommendationItem, isTopads: Boolean, position: String) {
        var list = RECOMMENDATION_LIST_TRACK
        if (isTopads) list += RECOMMENDATION_LIST_TOPADS_TRACK

        val map = DataLayer.mapOf(
            EVENT, PRODUCT_VIEW,
            EVENT_CATEGORY, PURCHASE_LIST_EVENT_CATEGORY,
            EVENT_ACTION, VIEW_RECOMMENDATION,
            EVENT_LABEL, EVENT_LABEL_RECOMMENDATION,
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            USER_ID, userId,
            BUSINESS_UNIT, ORDER_MANAGEMENT,
            ITEM_LIST, list,
            ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS, DataLayer.listOf(convertRecommendationItemToDataImpressionObject(recommendationItem, position))
            )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun convertRecommendationItemToDataImpressionObject(recommendationItem: RecommendationItem, position: String): Any  {
        return DataLayer.mapOf(
            ITEM_NAME, recommendationItem.name,
            ITEM_ID, recommendationItem.productId.toString(),
            PRICE, recommendationItem.price,
            ITEM_BRAND, "",
            ITEM_CATEGORY, recommendationItem.categoryBreadcrumbs,
            ITEM_VARIANT, "",
            INDEX, position,
            DIMENSION87, "",
            DIMENSION88, ""
        )
    }

    fun productClickRecommendation(click: ECommerceClick.Products, isTopads: Boolean, userId: String) {
        var list = RECOMMENDATION_LIST_TRACK
        if (isTopads) list += RECOMMENDATION_LIST_TOPADS_TRACK

        val arrayListBundleItems = arrayListOf<Bundle>()
        val bundleClick = Bundle().apply {
            putString(ITEM_NAME, click.name)
            putString(ITEM_ID, click.id)
            putString(PRICE, click.price)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, click.category)
            putString(ITEM_VARIANT, "")
            putString(INDEX, click.position)
            putString(DIMENSION87, "")
            putString(DIMENSION88, "")
        }
        arrayListBundleItems.add(bundleClick)

        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_CATEGORY, PURCHASE_LIST_EVENT_CATEGORY)
            putString(EVENT_ACTION, CLICK_RECOMMENDATION)
            putString(EVENT_LABEL, EVENT_LABEL_RECOMMENDATION)
            putString(SCREEN_NAME, ORDER_LIST_SCREEN_NAME)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(BUSINESS_UNIT, ORDER_MANAGEMENT)
            putString(ITEM_LIST, list)
            putParcelableArrayList(ITEMS, arrayListBundleItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    fun productAtcRecommendation(userId: String, listProduct: ArrayList<ECommerceAddRecommendation.Add.ActionField.Product>, isTopads: Boolean) {
        var list = RECOMMENDATION_LIST_TRACK
        if (isTopads) list += RECOMMENDATION_LIST_TOPADS_TRACK

        val arrayListBundleItems = arrayListOf<Bundle>()
        listProduct.forEach { product ->
            val bundleProduct = Bundle().apply {
                putString(ITEM_NAME, product.name)
                putString(ITEM_ID, product.id)
                putString(PRICE, product.price)
                putString(ITEM_BRAND, "")
                putString(ITEM_CATEGORY, product.category)
                putString(ITEM_VARIANT, "")
                putString(QUANTITY, product.quantity)
                putString(DIMENSION45, product.dimension45)
                putString(DIMENSION40, list)
            }
            arrayListBundleItems.add(bundleProduct)
        }

        val bundle = Bundle().apply {
            putString(EVENT, ADD_TO_CART_V5)
            putString(EVENT_CATEGORY, PURCHASE_LIST_EVENT_CATEGORY)
            putString(EVENT_ACTION, CLICK_ATC_RECOMMENDATION)
            putString(EVENT_LABEL, EVENT_LABEL_RECOMMENDATION)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(BUSINESS_UNIT, ORDER_MANAGEMENT)
            putParcelableArrayList(ITEMS, arrayListBundleItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART_V5, bundle)
    }

    fun orderDetailOpenScreenEvent() {
        val bundle = Bundle().apply {
            putString(EVENT, OPEN_SCREEN)
            putString(SCREEN_NAME, ORDER_DETAIL)
            putString(IS_LOGGED_IN_STATUS, TRUE)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, bundle)
    }
}