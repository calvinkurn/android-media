package com.tokopedia.tokopedianow.shoppinglist.analytic

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_SEE_DETAIL_ON_CART_PRODUCT_WIDGET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_IMPRESS_CART_PRODUCT_WIDGET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.CATEGORY.EVENT_CATEGORY_TOKONOW_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_SEE_DETAIL_ON_CART_PRODUCT_WIDGET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_IMPRESS_CART_PRODUCT_WIDGET
import com.tokopedia.track.builder.Tracker

class ShoppingListCartProductAnalytic {
    // Tracker ID: 50069
    fun trackImpressCartProductWidget() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESS_CART_PRODUCT_WIDGET)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESS_CART_PRODUCT_WIDGET)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker ID: 50070
    fun trackClickSeeDetail() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_SEE_DETAIL_ON_CART_PRODUCT_WIDGET)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_SEE_DETAIL_ON_CART_PRODUCT_WIDGET)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
