package com.tokopedia.tokopedianow.shoppinglist.analytic

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_CLICK_BUY_MORE_ON_TOP_NAV
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.ACTION.EVENT_ACTION_IMPRESS_SHOPPING_LIST_PAGE
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.CATEGORY.EVENT_CATEGORY_TOKONOW_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_BUY_MORE_ON_TOP_NAV
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_CLICK_CHECKBOX_TOP_ALL_ON_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic.TRACKER_ID.TRACKER_ID_IMPRESS_SHOPPING_LIST_PAGE
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * NOW! Shopping List Page Tracker
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3695
 */

class ShoppingListAnalytic @Inject constructor(
    userSession: UserSessionInterface
) {
    internal object ACTION {
        const val EVENT_ACTION_IMPRESS_SHOPPING_LIST_PAGE = "impression shopping list page"
        const val EVENT_ACTION_CLICK_BUY_MORE_ON_TOP_NAV = "click beli lagi on top nav"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click product recommendation on product card recommendation widget"
        const val EVENT_ACTION_IMPRESS_PRODUCT_RECOMMENDATION = "impression product recommendation on product card recommendation widget"
        const val EVENT_ACTION_CLICK_ADD_SHOPPING_LIST_PRODUCT_RECOMMENDATION = "click list belanja on recommendation widget"
        const val EVENT_ACTION_CLICK_CHECKBOX_TOP_ALL_ON_SHOPPING_LIST = "click semua on shopping list widget"
        const val EVENT_ACTION_CLICK_CHECKBOX_ON_PRODUCT_SHOPPING_LIST = "click checklist box on shopping list widget"
        const val EVENT_ACTION_CLICK_DELETE_BUTTON_ON_SHOPPING_LIST = "click delete button on shopping list widget"
        const val EVENT_ACTION_IMPRESS_PRODUCT_ON_SHOPPING_LIST = "impression product on product card shopping list widget"
        const val EVENT_ACTION_CLICK_PRODUCT_ON_SHOPPING_LIST = "click product on product card shopping list widget"
        const val EVENT_ACTION_IMPRESS_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET = "impression product on product card pilihan lain bottom sheet"
        const val EVENT_ACTION_CLICK_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET = "click product on product card pilihan lain bottom sheet"
        const val EVENT_ACTION_CLICK_ADD_SHOPPING_LIST_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET = "click list belanja on pilihan lain bottom sheet"
        const val EVENT_ACTION_IMPRESS_MINI_CART_SHOPPING_LIST = "impression minicart shopping list"
        const val EVENT_ACTION_CLICK_ADD_TO_CART_ON_MINI_CART_SHOPPING_LIST = "click add to cart on minicart shopping list"
        const val EVENT_ACTION_IMPRESS_CART_PRODUCT_WIDGET = "impression product on product add to card widget"
        const val EVENT_ACTION_CLICK_SEE_DETAIL_ON_CART_PRODUCT_WIDGET = "click lihat detail button on product add to card widget"
        const val EVENT_ACTION_CLICK_ANOTHER_OPTION_ON_SHOPPING_LIST = "click pilihan lain on out of stock widget"
    }

    internal object CATEGORY {
        const val EVENT_CATEGORY_TOKONOW_SHOPPING_LIST = "tokonow - shopping list"
    }

    internal object TRACKER_ID {
        const val TRACKER_ID_IMPRESS_SHOPPING_LIST_PAGE = "50000"
        const val TRACKER_ID_CLICK_BUY_MORE_ON_TOP_NAV = "50001"
        const val TRACKER_ID_CLICK_PRODUCT_RECOMMENDATION = "50003"
        const val TRACKER_ID_IMPRESS_PRODUCT_RECOMMENDATION = "50004"
        const val TRACKER_ID_CLICK_ADD_SHOPPING_LIST_PRODUCT_RECOMMENDATION = "50005"
        const val TRACKER_ID_CLICK_CHECKBOX_TOP_ALL_ON_SHOPPING_LIST = "50006"
        const val TRACKER_ID_CLICK_CHECKBOX_ON_PRODUCT_SHOPPING_LIST = "50007"
        const val TRACKER_ID_CLICK_DELETE_BUTTON_ON_SHOPPING_LIST = "50008"
        const val TRACKER_ID_IMPRESS_PRODUCT_ON_SHOPPING_LIST = "50009"
        const val TRACKER_ID_CLICK_PRODUCT_ON_SHOPPING_LIST = "50010"
        const val TRACKER_ID_IMPRESS_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET = "50066"
        const val TRACKER_ID_CLICK_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET = "50067"
        const val TRACKER_ID_CLICK_ADD_SHOPPING_LIST_PRODUCT_ANOTHER_OPTION_BOTTOM_SHEET = "50065"
        const val TRACKER_ID_IMPRESS_MINI_CART_SHOPPING_LIST = "50055"
        const val TRACKER_ID_CLICK_ADD_TO_CART_ON_MINI_CART_SHOPPING_LIST = "50056"
        const val TRACKER_ID_IMPRESS_CART_PRODUCT_WIDGET = "50069"
        const val TRACKER_ID_CLICK_SEE_DETAIL_ON_CART_PRODUCT_WIDGET = "50070"
        const val TRACKER_ID_CLICK_ANOTHER_OPTION_ON_SHOPPING_LIST = "50059"
    }

    internal object VALUE {
        const val SHOPPING_LIST = "shopping list"
        const val RECOMMENDATION_FOR_YOU_IN_BAHASA = "rekomendasi untukmu"
        const val PRODUCT_CARD = "product card"
        const val SHOPPING_LIST_IN_BAHASA = "list belanja"
        const val CHECKLIST_BOX = "checklist box"
        const val DELETE = "delete"
        const val ANOTHER_OPTION_IN_BAHASA = "pilihan lain"
    }

    private val shoppingListHorizontalProductCardAnalytic: ShoppingListHorizontalProductCardAnalytic = ShoppingListHorizontalProductCardAnalytic(userSession.userId)
    private val shoppingListBottomBulkAtcAnalytic: ShoppingListBottomBulkAtcAnalytic = ShoppingListBottomBulkAtcAnalytic(userSession.userId)
    private val shoppingListCartProductAnalytic: ShoppingListCartProductAnalytic = ShoppingListCartProductAnalytic()

    fun getShoppingListHorizontalProductCardAnalytic() = shoppingListHorizontalProductCardAnalytic
    fun getShoppingListBottomBulkAtcAnalytic() = shoppingListBottomBulkAtcAnalytic
    fun getShoppingListCartProductAnalytic() = shoppingListCartProductAnalytic

    //Tracker ID: 50000
    fun trackImpressShoppingListPage() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESS_SHOPPING_LIST_PAGE)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESS_SHOPPING_LIST_PAGE)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    //Tracker ID: 50001
    fun trackClickBuyMoreOnTopNav() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_BUY_MORE_ON_TOP_NAV)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_BUY_MORE_ON_TOP_NAV)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    //Tracker ID: 50006
    fun trackClickCheckboxTopCheckAll() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(ACTION.EVENT_ACTION_CLICK_CHECKBOX_TOP_ALL_ON_SHOPPING_LIST)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_SHOPPING_LIST)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CHECKBOX_TOP_ALL_ON_SHOPPING_LIST)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun clear() {
        shoppingListHorizontalProductCardAnalytic.clear()
        shoppingListCartProductAnalytic.clear()
    }
}
