package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.CHECKOUT_STEP_1
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_CHECKOUT_OPTION_MINI_CART
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.KEY_CHECKOUT_OPTION
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.KEY_CHECKOUT_STEP
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.KEY_TRACKER_ID
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.addGeneralTracker
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.getItemATC
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.getProductIds
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.getPromotionMerchant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.constant.TrackerConstant

/**
 * Category
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053 17 - 19
 */

class TokoFoodCategoryAnalyticsOld: BaseTrackerConst() {

    companion object {
        private const val ATC_CATEGORY_TRACKER_ID = "32009"
    }

    fun clickMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalytics.EVENT_ACTION_CLICK_MERCHANT_LIST
            )
            putString(TrackAppUtils.EVENT_LABEL, "${merchant.additionalData.topTextBanner} - ${merchant.promo}")
            putString(TokoFoodAnalyticsConstants.TRACKER_ID, TokoFoodAnalyticsConstants.TRACKER_ID_32008)
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionMerchant(merchant, horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.PROMO_CLICK, eventDataLayer)
    }

    fun clickAtc(userId: String?, destinationId: String?, data: CheckoutTokoFoodData){
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_ORDER_MINICART)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        val items = getItemATC(data)
        eventDataLayer.putParcelableArrayList(TokoFoodAnalytics.KEY_ITEMS, items)
        eventDataLayer.addToCart(userId, destinationId, data.shop.shopId, data)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.BEGIN_CHECKOUT, eventDataLayer)
    }

    private fun Bundle.addToCart(userId: String?, destinationId: String?, shopId: String, data: CheckoutTokoFoodData): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.BEGIN_CHECKOUT)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE)
        this.putString(TrackerConstant.SHOP_ID, shopId)
        this.putString(TokoFoodAnalyticsConstants.PRODUCT_ID, getProductIds(data))
        this.putString(KEY_TRACKER_ID, ATC_CATEGORY_TRACKER_ID)
        this.putString(KEY_CHECKOUT_STEP, CHECKOUT_STEP_1)
        this.putString(KEY_CHECKOUT_OPTION, EVENT_CHECKOUT_OPTION_MINI_CART)
        return this
    }

    private fun Bundle.viewItem(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.VIEW_ITEM)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE)
        return this
    }

    private fun Bundle.selectContent(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, Event.SELECT_CONTENT)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE)
        return this
    }
}
