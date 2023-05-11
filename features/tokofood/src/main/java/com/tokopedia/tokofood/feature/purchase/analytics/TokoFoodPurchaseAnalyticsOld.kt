package com.tokopedia.tokofood.feature.purchase.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.constant.TrackerConstant

/**
 * https://mynakama.tokopedia.com/datatracker/requestdetail/3057
 */
object TokoFoodPurchaseAnalyticsOld: BaseTrackerConst() {

    fun sendLoadCheckoutTracking(data: CheckoutTokoFoodData,
                                 userId: String) {
        val totalProductPrice = data.shoppingSummary.costBreakdown.totalCartPrice.originalAmount
        val totalShippingPrice = data.shipping.price
        val totalPromotionPrice = data.shoppingSummary.discountBreakdown.sumOf { it.amount }
        val totalAllPrice = data.shoppingSummary.total.cost
        val foodIdArray = data.availableSection.products.map { it.productId }.distinct()
        val eventLabel =
            "${totalProductPrice}_${totalShippingPrice}_${totalPromotionPrice}_${totalAllPrice}_${foodIdArray}"

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalytics.EVENT_CHECKOUT_PROGRESS)
            putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CHECKOUT_PAGE)
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_VIEW_CHECKOUT)
            putString(TrackAppUtils.EVENT_LABEL, eventLabel)
            putString(TrackerConstant.BUSINESS_UNIT, TokoFoodAnalytics.PHYSICAL_GOODS)
            putString(TrackerConstant.CURRENT_SITE, TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            putString(TrackerConstant.USERID, userId)
            putString(TokoFoodAnalytics.KEY_CHECKOUT_OPTION, TokoFoodAnalytics.VIEW_CHECKOUT)
            putString(TokoFoodAnalytics.KEY_CHECKOUT_STEP, TokoFoodAnalytics.CHECKOUT_STEP_2)
            val items = getItemBundles(data)
            putParcelableArrayList(TokoFoodAnalytics.KEY_ITEMS, items)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalytics.EVENT_CHECKOUT_PROGRESS, dataLayer)
    }

    fun sendSuccessChoosePayment(data: CheckoutTokoFoodData,
                                 userId: String) {
        val totalProductPrice = data.shoppingSummary.costBreakdown.totalCartPrice.originalAmount
        val totalShippingPrice = data.shipping.price
        val totalPromotionPrice = data.shoppingSummary.discountBreakdown.sumOf { it.amount }
        val totalAllPrice = data.shoppingSummary.total.cost
        val foodIdArray = data.availableSection.products.map { it.productId }.distinct()
        val eventLabel =
            "${totalProductPrice}_${totalShippingPrice}_${totalPromotionPrice}_${totalAllPrice}_${foodIdArray}"

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalytics.EVENT_CHECKOUT_PROGRESS)
            putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CHECKOUT_PAGE)
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_PAYMENT)
            putString(TrackAppUtils.EVENT_LABEL, eventLabel)
            putString(TrackerConstant.BUSINESS_UNIT, TokoFoodAnalytics.PHYSICAL_GOODS)
            putString(TrackerConstant.CURRENT_SITE, TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            putString(TrackerConstant.USERID, userId)
            putString(TokoFoodAnalytics.KEY_CHECKOUT_OPTION, TokoFoodAnalytics.CLICK_SUCCESS_PAYMENT)
            putString(TokoFoodAnalytics.KEY_CHECKOUT_STEP, TokoFoodAnalytics.CHECKOUT_STEP_4)
            val items = getItemBundles(data)
            putParcelableArrayList(TokoFoodAnalytics.KEY_ITEMS, items)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalytics.EVENT_CHECKOUT_PROGRESS, dataLayer)
    }

    fun sendLoadPromoPageTracking() {
        Tracker.Builder()
            .setEvent(TokoFoodAnalytics.EVENT_VIEW_PG_IRIS)
            .setEventAction(TokoFoodAnalytics.EVENT_ACTION_VIEW_PROMO_PAGE)
            .setEventCategory(TokoFoodAnalytics.EVENT_CATEGORY_PROMO_PAGE)
            .setEventLabel(String.EMPTY)
            .setBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .setCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .build()
            .send()
    }

    private fun getItemBundles(data: CheckoutTokoFoodData): ArrayList<Bundle> {
        val itemBundles = arrayListOf<Bundle>()
        itemBundles.addAll(
            data.availableSection.products.map {
                Bundle().apply {
                    putString(
                        TokoFoodAnalytics.KEY_DIMENSION_49,
                        data.shoppingSummary.discountBreakdown.firstOrNull()?.discountId.orEmpty()
                    )
                    putString(TokoFoodAnalytics.KEY_DIMENSION_79, data.shop.shopId)
                    putString(Items.ITEM_ID, it.productId)
                    putString(Items.ITEM_CATEGORY, it.categoryId)
                    putDouble(Items.PRICE, it.price)
                    putInt(TokoFoodAnalytics.KEY_QUANTITY, it.quantity)
                    putString(TrackerConstant.SHOP_ID, data.shop.shopId)
                    putString(TokoFoodAnalytics.KEY_SHOP_NAME, data.shop.name)
                }
            }
        )
        return itemBundles
    }

}
