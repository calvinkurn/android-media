package com.tokopedia.autocompletecomponent.initialstate.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.ACTION_FIELD
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.BUSINESS_UNIT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CLICK
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CURRENT_SITE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Category.TOP_NAV
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Category.TOP_NAV_TOKO_NOW
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.ECOMMERCE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_ACTION
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_CATEGORY
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_LABEL
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_TOKO_NOW
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_TOP_NAV
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.PRODUCT_CLICK
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.VIEW_TOP_NAV_IRIS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.LIST
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PAGE_SOURCE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PRODUCTS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.SEARCH
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.USER_ID
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_INITIAL_STATE_PRODUCT_LINE
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_RECENT_VIEW_PRODUCT
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_REFRESH_POPULAR_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_REFRESH_TOKONOW_POPULAR_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_SEE_MORE_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.IMPRESSION_SEE_MORE_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Misc.HOMEPAGE
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Misc.PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Misc.RECENT_VIEW_ACTION_FIELD
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

open class InitialStateTracking(
    protected val iris: Iris,
) {

    companion object {
        object Action {
            const val IMPRESSION_SEE_MORE_RECENT_SEARCH = "impression see more - recent search"
            const val CLICK_SEE_MORE_RECENT_SEARCH = "click see more - recent search"
            const val CLICK_RECENT_VIEW_PRODUCT = "click - recent view product"
            const val CLICK_INITIAL_STATE_PRODUCT_LINE = "click - product initial state campaign"
            const val CLICK_REFRESH_POPULAR_SEARCH = "click refresh on popular search"
            const val CLICK_REFRESH_TOKONOW_POPULAR_SEARCH =
                "click - refresh popular search - initial state"
        }

        object Misc {
            const val HOMEPAGE = "homepage"
            const val RECENT_VIEW_ACTION_FIELD = "/search - recentview - product"
            const val PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD = "/search - product initial state campaign"
        }
    }

    private val analytics: Analytics
        get() = TrackApp.getInstance().gtm

    open fun impressedRecentView(
        searchComponentTrackingList: List<SearchComponentTracking>,
        list: List<Any>
    ) {
        searchComponentTrackingList.forEach { it.impress(iris) }
    }

    open fun impressedRecentSearch(
        searchComponentTrackingList: List<SearchComponentTracking>,
        list: List<Any>,
        keyword: String,
    ) {
        searchComponentTrackingList.forEach { it.impress(iris) }
    }

    open fun impressedDynamicSection(
        searchComponentTrackingList: List<SearchComponentTracking>,
        model: DynamicInitialStateItemTrackingModel
    ) {
        searchComponentTrackingList.forEach { it.impress(iris) }
    }

    open fun impressedSeeMoreRecentSearch(userId: String) {
        val map = DataLayer.mapOf(
            EVENT, VIEW_TOP_NAV_IRIS,
            EVENT_CATEGORY, "$TOP_NAV - /",
            EVENT_ACTION, IMPRESSION_SEE_MORE_RECENT_SEARCH,
            EVENT_LABEL, "",
            BUSINESS_UNIT, SEARCH,
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            USER_ID, userId
        )

        iris.saveEvent(map)
    }

    open fun eventClickRecentSearch(
        searchComponentTracking: SearchComponentTracking,
        label: String,
        pageSource: String,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickRecentShop(
        searchComponentTracking: SearchComponentTracking,
        label: String,
        userId: String,
        pageSource: String
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickSeeMoreRecentSearch(userId: String) {
        analytics.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_TOP_NAV,
                EVENT_CATEGORY, "$TOP_NAV - /",
                EVENT_ACTION, CLICK_SEE_MORE_RECENT_SEARCH,
                EVENT_LABEL, "",
                BUSINESS_UNIT, SEARCH,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
            )
        )
    }

    open fun eventClickDynamicSection(
        searchComponentTracking: SearchComponentTracking,
        userId: String,
        label: String,
        type: String,
        pageSource: String
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickCuratedCampaignCard(
        searchComponentTracking: SearchComponentTracking,
        userId: String,
        label: String,
        type: String,
        campaignCode: String
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun impressedCuratedCampaign(
        searchComponentTracking: SearchComponentTracking,
        userId: String,
        label: String,
        type: String,
        campaignCode: String
    ) {
        searchComponentTracking.impress(iris)
    }

    open fun eventClickRecentView(productDataLayer: Any, label: String) {
        analytics.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, TOP_NAV,
                EVENT_ACTION, CLICK_RECENT_VIEW_PRODUCT,
                EVENT_LABEL, label,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, RECENT_VIEW_ACTION_FIELD),
                        PRODUCTS, DataLayer.listOf(productDataLayer)
                    )
                )
            )
        )
    }

    open fun eventClickInitialStateProductLine(
        productDataLayer: Any,
        userId: String,
        label: String,
        pageSource: String
    ) {
        analytics.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, TOP_NAV,
                EVENT_ACTION, CLICK_INITIAL_STATE_PRODUCT_LINE,
                EVENT_LABEL, label,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD),
                        PRODUCTS, DataLayer.listOf(productDataLayer)
                    )
                ),
                BUSINESS_UNIT, SEARCH,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId,
                PAGE_SOURCE, pageSource
            )
        )
    }

    open fun eventClickRefreshPopularSearch() {
        analytics.sendGeneralEvent(
            CLICK_TOP_NAV,
            "$TOP_NAV - $HOMEPAGE",
            CLICK_REFRESH_POPULAR_SEARCH,
            ""
        )
    }

    open fun eventClickRefreshTokoNowPopularSearch() {
        analytics.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_TOKO_NOW,
                EVENT_CATEGORY, TOP_NAV_TOKO_NOW,
                EVENT_ACTION, CLICK_REFRESH_TOKONOW_POPULAR_SEARCH,
                EVENT_LABEL, "",
                BUSINESS_UNIT, SEARCH,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE
            )
        )
    }

    open fun eventClickTokoNowPopularSearch(
        searchComponentTracking: SearchComponentTracking,
        label: String,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickSearchBarEducation(item: BaseItemInitialStateSearch) {
        item.click(analytics)
    }
}
