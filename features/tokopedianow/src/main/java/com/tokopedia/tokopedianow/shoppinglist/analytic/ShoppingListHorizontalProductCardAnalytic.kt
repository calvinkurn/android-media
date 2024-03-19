package com.tokopedia.tokopedianow.shoppinglist.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_40
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_56
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_98
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.NULL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.SLASH
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_ADD_SHOPPING_LIST_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_ADD_SHOPPING_LIST_PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_ANOTHER_OPTION_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_CHECKBOX_ON_PRODUCT_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_DELETE_BUTTON_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_PRODUCT_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_IMPRESS_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_IMPRESS_PRODUCT_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_IMPRESS_PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.CATEGORY.EVENT_CATEGORY_TOKONOW_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_ADD_SHOPPING_LIST_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_ADD_SHOPPING_LIST_PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_ANOTHER_OPTION_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_CHECKBOX_ON_PRODUCT_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_DELETE_BUTTON_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_PRODUCT_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_IMPRESS_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_IMPRESS_PRODUCT_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_IMPRESS_PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.VALUE.ANOTHER_OPTION_IN_BAHASA
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.VALUE.CHECKLIST_BOX
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.VALUE.DELETE
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.VALUE.PRODUCT_CARD
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.VALUE.RECOMMENDATION_FOR_YOU_IN_BAHASA
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.VALUE.SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.VALUE.SHOPPING_LIST_IN_BAHASA
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION_ADDED
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL

class ShoppingListHorizontalProductCardAnalytic(
    private val userId: String
) {
    private val impressionProducts: HashMap<String, ShoppingListProductLayoutType> = hashMapOf()

    private fun getItem(
        index: Int,
        productId: String,
        productName: String,
        price: String,
        section: String,
        widget: String,
        warehouseId: String,
        isAvailable: Boolean
    ): Bundle = Bundle().apply {
        putString(KEY_DIMENSION_40, joinDash(SLASH+TOKONOW, SHOPPING_LIST, section, widget, productId))
        putString(KEY_DIMENSION_56, warehouseId)
        putBoolean(KEY_DIMENSION_98, isAvailable)
        putInt(KEY_INDEX, index.getTrackerPosition())
        putString(KEY_ITEM_BRAND, NULL)
        putString(KEY_ITEM_CATEGORY, NULL)
        putString(KEY_ITEM_ID, productId)
        putString(KEY_ITEM_NAME, productName)
        putString(KEY_ITEM_VARIANT, NULL)
        putDouble(KEY_PRICE, price.getDigits().orZero().toDouble())
    }

    private fun trackProduct(
        event: String,
        eventAction: String,
        eventLabel: String,
        section: String,
        widget: String,
        product: ShoppingListHorizontalProductCardItemUiModel,
        trackerId: String
    ) {
        val dataLayer = Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, eventAction)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            putString(EVENT_LABEL, eventLabel)
            putString(KEY_TRACKER_ID, trackerId)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_ITEM_LIST, joinDash(SLASH+TOKONOW, SHOPPING_LIST, section, widget, product.id))
            putString(KEY_USER_ID, userId)
            putParcelableArrayList(KEY_ITEMS,
                arrayListOf(
                    getItem(
                        index = product.index,
                        productId = product.id,
                        productName = product.name,
                        price = product.price,
                        section = section,
                        widget = widget,
                        warehouseId = product.warehouseId,
                        isAvailable = product.productLayoutType != UNAVAILABLE_SHOPPING_LIST
                    )
                )
            )
        }
        getTracker()
            .sendEnhanceEcommerceEvent(
                event,
                dataLayer
            )
    }

    //Tracker ID: 50005
    fun trackClickAddToShoppingListOnProduct(
        product: ShoppingListHorizontalProductCardItemUiModel,
        isFromBottomSheet: Boolean = false
    ) {
        val eventAction: String
        val trackerId: String
        val section: String

        if (isFromBottomSheet) {
            eventAction = EVENT_ACTION_CLICK_ADD_SHOPPING_LIST_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
            trackerId = TRACKER_ID_CLICK_ADD_SHOPPING_LIST_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
            section = ANOTHER_OPTION_IN_BAHASA
        } else {
            eventAction = EVENT_ACTION_CLICK_ADD_SHOPPING_LIST_PRODUCT_RECOMMENDATION
            trackerId = TRACKER_ID_CLICK_ADD_SHOPPING_LIST_PRODUCT_RECOMMENDATION
            section = RECOMMENDATION_FOR_YOU_IN_BAHASA
        }

        trackProduct(
            event = EVENT_SELECT_CONTENT,
            eventAction = eventAction,
            eventLabel = product.id,
            section = section,
            widget = SHOPPING_LIST_IN_BAHASA,
            product = product,
            trackerId = trackerId
        )
    }

    //Tracker ID: 50007
    fun trackClickCheckboxOnProduct(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        trackProduct(
            event = EVENT_SELECT_CONTENT,
            eventAction = EVENT_ACTION_CLICK_CHECKBOX_ON_PRODUCT_SHOPPING_LIST,
            eventLabel = product.id,
            section = SHOPPING_LIST_IN_BAHASA,
            widget = CHECKLIST_BOX,
            product = product,
            trackerId = TRACKER_ID_CLICK_CHECKBOX_ON_PRODUCT_SHOPPING_LIST
        )
    }

    //Tracker ID: 50008
    fun trackClickDeleteButtonOnProduct(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        trackProduct(
            event = EVENT_SELECT_CONTENT,
            eventAction = EVENT_ACTION_CLICK_DELETE_BUTTON_ON_SHOPPING_LIST,
            eventLabel = product.id,
            section = SHOPPING_LIST_IN_BAHASA,
            widget = DELETE,
            product = product,
            trackerId = TRACKER_ID_CLICK_DELETE_BUTTON_ON_SHOPPING_LIST
        )
    }

    //Tracker ID: 50059
    fun trackClickAnotherOptionOnProduct(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        trackProduct(
            event = EVENT_SELECT_CONTENT,
            eventAction = EVENT_ACTION_CLICK_ANOTHER_OPTION_ON_SHOPPING_LIST,
            eventLabel = product.id,
            section = SHOPPING_LIST_IN_BAHASA,
            widget = ANOTHER_OPTION_IN_BAHASA,
            product = product,
            trackerId = TRACKER_ID_CLICK_ANOTHER_OPTION_ON_SHOPPING_LIST
        )
    }

    //Tracker ID: 50003, 50010, 50067
    fun trackClickProduct(
        product: ShoppingListHorizontalProductCardItemUiModel,
        isFromBottomSheet: Boolean = false
    ) {
        val eventAction: String
        val trackerId: String
        val section: String

        when(product.productLayoutType) {
            AVAILABLE_SHOPPING_LIST, UNAVAILABLE_SHOPPING_LIST -> {
                eventAction = EVENT_ACTION_CLICK_PRODUCT_ON_SHOPPING_LIST
                trackerId = TRACKER_ID_CLICK_PRODUCT_ON_SHOPPING_LIST
                section = SHOPPING_LIST_IN_BAHASA
            }
            PRODUCT_RECOMMENDATION, PRODUCT_RECOMMENDATION_ADDED -> {
                if (isFromBottomSheet) {
                    eventAction = EVENT_ACTION_CLICK_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
                    trackerId = TRACKER_ID_CLICK_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
                    section = ANOTHER_OPTION_IN_BAHASA
                } else {
                    eventAction = EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION
                    trackerId = TRACKER_ID_CLICK_PRODUCT_RECOMMENDATION
                    section = RECOMMENDATION_FOR_YOU_IN_BAHASA
                }
            }
        }

        trackProduct(
            event = EVENT_SELECT_CONTENT,
            eventAction = eventAction,
            eventLabel = product.id,
            section = section,
            widget = PRODUCT_CARD,
            product = product,
            trackerId = trackerId
        )
    }

    //Tracker ID: 50004, 50009, 50066
    fun trackImpressProduct(
        product: ShoppingListHorizontalProductCardItemUiModel,
        isFromBottomSheet: Boolean = false
    ) {
        val eventAction: String
        val trackerId: String
        val section: String

        when(product.productLayoutType) {
            AVAILABLE_SHOPPING_LIST, UNAVAILABLE_SHOPPING_LIST -> {
                eventAction = EVENT_ACTION_IMPRESS_PRODUCT_ON_SHOPPING_LIST
                trackerId = TRACKER_ID_IMPRESS_PRODUCT_ON_SHOPPING_LIST
                section = SHOPPING_LIST_IN_BAHASA
            }
            PRODUCT_RECOMMENDATION, PRODUCT_RECOMMENDATION_ADDED -> {
                if (isFromBottomSheet) {
                    eventAction = EVENT_ACTION_IMPRESS_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
                    trackerId = TRACKER_ID_IMPRESS_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET
                    section = ANOTHER_OPTION_IN_BAHASA
                } else {
                    eventAction = EVENT_ACTION_IMPRESS_PRODUCT_RECOMMENDATION
                    trackerId = TRACKER_ID_IMPRESS_PRODUCT_RECOMMENDATION
                    section = RECOMMENDATION_FOR_YOU_IN_BAHASA
                }
            }
        }

        if (impressionProducts[product.id] == null) {
            trackProduct(
                event = EVENT_VIEW_ITEM_LIST,
                eventAction = eventAction,
                eventLabel = String.EMPTY,
                section = section,
                widget = PRODUCT_CARD,
                product = product,
                trackerId = trackerId
            )
            impressionProducts[product.id] = product.productLayoutType
        }
    }

    fun clear() {
        impressionProducts.clear()
    }
}
