package com.tokopedia.tokopedianow.shoppinglist.analytic

import android.os.Bundle
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.NULL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_ADD_TO_CART_ON_MINI_CART_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_IMPRESS_MINI_CART_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.CATEGORY.EVENT_CATEGORY_TOKONOW_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_ADD_TO_CART_ON_MINI_CART_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_IMPRESS_MINI_CART_SHOPPING_LIST
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.builder.Tracker

class ShoppingListBottomBulkAtcAnalytic(
    private val userId: String
) {
    private fun getItem(
        productId: String,
        productName: String,
        price: String,
        quantity: Int,
        shopId: String,
    ): Bundle = Bundle().apply {
        putString(KEY_CATEGORY_ID, NULL)
        putString(KEY_ITEM_BRAND, NULL)
        putString(KEY_ITEM_CATEGORY, NULL)
        putString(KEY_ITEM_ID, productId)
        putString(KEY_ITEM_NAME, productName)
        putString(KEY_ITEM_VARIANT, NULL)
        putDouble(KEY_PRICE, price.getDigits().orZero().toDouble())
        putString(KEY_QUANTITY, quantity.toString())
        putString(KEY_SHOP_ID, shopId)
        putString(KEY_SHOP_NAME, NULL)
        putString(KEY_SHOP_TYPE, NULL)
    }

    // Tracker ID: 50056
    fun trackClickAtcMiniCartShoppingList(
        atcMultiParams: List<AddToCartMultiParam>
    ) {
        val dataLayer = Bundle().apply {
            putString(EVENT, EVENT_ADD_TO_CART)
            putString(EVENT_ACTION, EVENT_ACTION_CLICK_ADD_TO_CART_ON_MINI_CART_SHOPPING_LIST)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            putString(EVENT_LABEL, String.EMPTY)
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_ADD_TO_CART_ON_MINI_CART_SHOPPING_LIST)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userId)
            putParcelableArrayList(KEY_ITEMS,
                ArrayList(
                    atcMultiParams.map {
                        getItem(
                            productId = it.productId,
                            productName = it.productName,
                            price = it.productPrice.toString(),
                            quantity = it.qty,
                            shopId = it.shopId
                        )
                    }
                )
            )
        }
        getTracker()
            .sendEnhanceEcommerceEvent(
                EVENT_ADD_TO_CART,
                dataLayer
            )
    }

    // Tracker ID: 50055
    fun trackImpressMiniCartShoppingList() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESS_MINI_CART_SHOPPING_LIST)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESS_MINI_CART_SHOPPING_LIST)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
