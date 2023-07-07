package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_CLICK_CART_BUTTON
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_CLICK_WIDGET_CHOOSE_ADDRESS
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_TOP_NAV_CATEGORY_PAGE_L1
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.TRACKER_ID.ID_CLICK_CART_BUTTON
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.TRACKER_ID.ID_CLICK_CHOOSE_ADDRESS_WIDGET
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.TRACKER_ID.ID_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.ADDITIONAL_MAP_CATEGORY
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.ADDITIONAL_MAP_CATEGORY_ID
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.SCREEN_NAME_CATEGORY
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.SCREEN_NAME_TOKONOW_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Category Revamp L1 Tracker
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3980
 **/

class CategoryAnalytic @Inject constructor(
    private val userSession: UserSessionInterface,
    private val addressData: TokoNowLocalAddress
) {
    internal object ACTION {
        const val EVENT_ACTION_CLICK_OTHER_CATEGORIES = "click kategori lain"
        const val EVENT_ACTION_IMPRESS_CATEGORY_L2 = "impression category widget - l2"
        const val EVENT_ACTION_CLICK_CATEGORY_L2 = "click category widget - l2"
        const val EVENT_ACTION_IMPRESS_PRODUCT_CAROUSEL = "impression product carousel"
        const val EVENT_ACTION_CLICK_PRODUCT_CAROUSEL = "click product carousel"
        const val EVENT_ACTION_IMPRESS_PRODUCT_SHOWCASE = "impression product in showcase - l2"
        const val EVENT_ACTION_CLICK_ARROW_BUTTON_SHOWCASE = "click arrow button showcase - l2"
        const val EVENT_ACTION_CLICK_PRODUCT_SHOWCASE = "click product showcase - l2"
        const val EVENT_ACTION_IMPRESS_CATEGORY_RECOM_WIDGET = "impression category recom widget"
        const val EVENT_ACTION_CLICK_CATEGORY_RECOM_WIDGET = "click category recom widget"
        const val EVENT_ACTION_CLICK_ATC_ON_SHOWCASE = "click atc on showcase - l2"
        const val EVENT_ACTION_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET = "click atc carousel widget"
        const val EVENT_ACTION_CLICK_CART_BUTTON = "click cart button"
        const val EVENT_ACTION_CLICK_SEARCH_BAR = "click search bar"
        const val EVENT_ACTION_CLICK_PRODUCT = "click product oos bottomsheet"
        const val EVENT_ACTION_CLICK_ADD_TO_CART = "add to cart product oos bottomsheet"
        const val EVENT_ACTION_CLICK_CLOSE_BOTTOMSHEET = "click close bottomsheet"
        const val EVENT_ACTION_IMPRESSION_EMPTY_STATE = "impression empty state bottomsheet"
        const val EVENT_ACTION_CLICK_WIDGET_CHOOSE_ADDRESS = "click widget choose address"
    }

    internal object CATEGORY {
        const val EVENT_CATEGORY_PAGE = "tokonow - category page"
        const val EVENT_CATEGORY_PAGE_L1 = "tokonow - category page - l1"
        const val EVENT_CATEGORY_TOP_NAV_CATEGORY_PAGE_L1 = "tokonow - top nav - category page - l1"
    }

    internal object VALUE {
        const val ITEM_LIST_CATEGORY_L1 = "category l1"
        const val ITEM_LIST_SHOWCASE = "showcase"
        const val ITEM_LIST_RECOMPRODUCT = "recomproduct"
        const val SCREEN_NAME_CATEGORY = "tokonow/category/%s"
        const val SCREEN_NAME_TOKONOW_OOC = "tokonow ooc - tokonow/category"
        const val ADDITIONAL_MAP_CATEGORY = "category"
        const val ADDITIONAL_MAP_CATEGORY_ID = "categoryId"
        const val PAGE_NAME_TOKOPEDIA_NOW = "tokopedia now"
        const val DEFAULT_NULL_VALUE = "null"
    }

    internal object TRACKER_ID {
        const val ID_CLICK_OTHER_CATEGORIES  = "43848"
        const val ID_IMPRESS_CATEGORY_L2 = "43849"
        const val ID_CLICK_CATEGORY_L2 = "43850"
        const val ID_IMPRESS_PRODUCT_CAROUSEL = "43851"
        const val ID_CLICK_PRODUCT_CAROUSEL = "43852"
        const val ID_IMPRESS_PRODUCT_SHOWCASE = "43853"
        const val ID_CLICK_ARROW_BUTTON_SHOWCASE = "43854"
        const val ID_CLICK_PRODUCT_SHOWCASE = "43855"
        const val ID_IMPRESS_CATEGORY_RECOM_WIDGET = "43858"
        const val ID_CLICK_CATEGORY_RECOM_WIDGET = "43859"
        const val ID_CLICK_ATC_ON_SHOWCASE= "43860"
        const val ID_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET = "43862"
        const val ID_CLICK_CHOOSE_ADDRESS_WIDGET = "44681"
        const val ID_CLICK_CART_BUTTON = "44680"
        const val ID_CLICK_SEARCH_BAR = "44678"
    }

    val categoryTitleAnalytics: CategoryTitleAnalytic
        get() = CategoryTitleAnalytic()

    val categoryNavigationAnalytic: CategoryNavigationAnalytic
        get() = CategoryNavigationAnalytic()

    val categoryProductRecommendationAnalytic: CategoryProductRecommendationAnalytic
        get() = CategoryProductRecommendationAnalytic(userSession.userId)

    val categoryMenuAnalytic: CategoryMenuAnalytic
        get() = CategoryMenuAnalytic()

    val categoryShowcaseAnalytic: CategoryShowcaseAnalytic
        get() = CategoryShowcaseAnalytic(userSession.userId)

    val categorySharingExperienceAnalytic: CategorySharingExperienceAnalytic
        get() = CategorySharingExperienceAnalytic(userSession.userId)

    val categoryOosProductAnalytic: CategoryOosProductAnalytic
        get() = CategoryOosProductAnalytic()

    val productAdsAnalytic: CategoryProductAdsAnalytic
        get() = CategoryProductAdsAnalytic(userSession, addressData)

    fun sendOpenScreenEvent(slug: String, id: String, name: String, isLoggedInStatus: Boolean) {
        TokoNowCommonAnalytics.onOpenScreen(
            isLoggedInStatus = isLoggedInStatus,
            screenName =  String.format(SCREEN_NAME_CATEGORY, slug),
            additionalMap = mutableMapOf(
                Pair(ADDITIONAL_MAP_CATEGORY, name),
                Pair(ADDITIONAL_MAP_CATEGORY_ID, id)
            )
        )
    }

    fun sendOocOpenScreenEvent(isLoggedInStatus: Boolean) {
        TokoNowCommonAnalytics.onOpenScreen(
            isLoggedInStatus = isLoggedInStatus,
            screenName = SCREEN_NAME_TOKONOW_OOC
        )
    }

    // Tracker ID: 44678
    fun sendClickSearchBarEvent (
        categoryIdL1: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_SEARCH_BAR)
            .setEventCategory(EVENT_CATEGORY_TOP_NAV_CATEGORY_PAGE_L1)
            .setEventLabel(joinDash(categoryIdL1, warehouseId))
            .setCustomProperty(KEY_TRACKER_ID, ID_CLICK_SEARCH_BAR)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker ID: 44680
    fun sendClickCartButtonEvent(
        categoryIdL1: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_CART_BUTTON)
            .setEventCategory(EVENT_CATEGORY_TOP_NAV_CATEGORY_PAGE_L1)
            .setEventLabel(joinDash(categoryIdL1, warehouseId))
            .setCustomProperty(KEY_TRACKER_ID, ID_CLICK_CART_BUTTON)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker ID: 44681
    fun sendClickWidgetChooseAddressEvent(
        categoryIdL1: String,
        warehouseId: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_WIDGET_CHOOSE_ADDRESS)
            .setEventCategory(EVENT_CATEGORY_TOP_NAV_CATEGORY_PAGE_L1)
            .setEventLabel(joinDash(categoryIdL1, warehouseId))
            .setCustomProperty(KEY_TRACKER_ID, ID_CLICK_CHOOSE_ADDRESS_WIDGET)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
