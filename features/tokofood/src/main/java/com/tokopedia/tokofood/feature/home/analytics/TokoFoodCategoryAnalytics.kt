package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalytics.addGeneralTracker
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalytics.getItemATC
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalytics.getPromotionMerchant
import com.tokopedia.tokofood.feature.home.domain.data.Merchant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Category
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053 17 - 19
 */

class TokoFoodCategoryAnalytics: BaseTrackerConst() {

    fun clickMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalytics.EVENT_ACTION_CLICK_MERCHANT_LIST
            )
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionMerchant(merchant, horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, eventDataLayer)
    }

    fun clickAtc(userId: String?, destinationId: String?, data: CheckoutTokoFoodData){
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_ORDER_MINICART)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        val items = getItemATC(data)
        eventDataLayer.putParcelableArrayList(TokoFoodAnalytics.KEY_ITEMS, items)
        eventDataLayer.addToCart(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.ADD_TO_CART, eventDataLayer)
    }

    private fun Bundle.addToCart(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.ADD_TO_CART)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE)
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