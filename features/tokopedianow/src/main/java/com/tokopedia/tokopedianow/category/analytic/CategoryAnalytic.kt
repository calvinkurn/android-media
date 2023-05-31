package com.tokopedia.tokopedianow.category.analytic

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_CLICK_CART_BUTTON_TOP_NAV
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_TOP_NAV_TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.ADDITIONAL_MAP_CATEGORY
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.ADDITIONAL_MAP_CATEGORY_ID
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.SCREEN_NAME_CATEGORY
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.VALUE.SCREEN_NAME_TOKONOW_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOP_NAV
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Category Revamp L1 Tracker
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3980
 **/

class CategoryAnalytic @Inject constructor(
    private val userSession: UserSessionInterface
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
        const val EVENT_ACTION_CLICK_CART_BUTTON_TOP_NAV = "click cart button top nav"
        const val EVENT_ACTION_CLICK_SEARCH_BAR = "click search bar"
        const val EVENT_ACTION_CLICK_PRODUCT = "click product oos bottomsheet"
        const val EVENT_ACTION_CLICK_ADD_TO_CART = "add to cart product oos bottomsheet"
        const val EVENT_ACTION_CLICK_CLOSE_BOTTOMSHEET = "click close bottomsheet"
        const val EVENT_ACTION_IMPRESSION_EMPTY_STATE = "impression empty state bottomsheet"
    }

    internal object CATEGORY {
        const val EVENT_CATEGORY_PAGE_L1 = "tokonow - category page - l1"
        const val EVENT_CATEGORY_TOP_NAV_TOKONOW_CATEGORY_PAGE =  "top nav - tokonow category page"
        const val EVENT_CATEGORY_TOKONOW_CATEGORY_PAGE = "tokonow category page"
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

    fun sendSearchBarClickEvent(categoryId: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOP_NAV,
                EVENT_ACTION, EVENT_ACTION_CLICK_SEARCH_BAR,
                EVENT_CATEGORY, EVENT_CATEGORY_TOP_NAV_TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendCartClickEvent(categoryId: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, EVENT_ACTION_CLICK_CART_BUTTON_TOP_NAV,
                EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }
}
