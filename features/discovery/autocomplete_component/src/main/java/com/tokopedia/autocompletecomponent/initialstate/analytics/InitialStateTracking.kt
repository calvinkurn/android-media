package com.tokopedia.autocompletecomponent.initialstate.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.ACTION_FIELD
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.BUSINESS_UNIT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CAMPAIGN_CODE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CLICK
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CURRENCYCODE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CURRENT_SITE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Category.TOP_NAV
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Category.TOP_NAV_TOKO_NOW
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.ECOMMERCE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_ACTION
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_CATEGORY
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_LABEL
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_SEARCH
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_TOKO_NOW
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_TOP_NAV
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.PRODUCT_CLICK
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.PRODUCT_VIEW_IRIS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.PROMO_VIEW_IRIS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.VIEW_TOP_NAV_IRIS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.IDR
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.IMPRESSIONS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.LIST
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PAGE_SOURCE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PRODUCTS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PROMOTIONS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PROMOVIEW
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.SCREEN_NAME
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.SEARCH
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.USER_ID
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_CURATED_CAMPAIGN
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_DYNAMIC_SECTION
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_INITIAL_STATE_PRODUCT_LINE
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_POPULAR_SEARCH_TOKONOW
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_RECENT_SHOP
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_RECENT_VIEW_PRODUCT
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_REFRESH_POPULAR_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_REFRESH_TOKONOW_POPULAR_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.CLICK_SEE_MORE_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.IMPRESSION_CURATED_CAMPAIGN
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.IMPRESSION_POPULAR_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.IMPRESSION_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.IMPRESSION_RECENT_VIEW
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Action.IMPRESSION_SEE_MORE_RECENT_SEARCH
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Category.INITIAL_STATE
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Category.TOP_NAV_INITIAL_STATE
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Misc.HOMEPAGE
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Misc.PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Misc.RECENT_VIEW_ACTION_FIELD
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingRollence
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import com.tokopedia.remoteconfig.RollenceKey.AUTOCOMPLETE_INITIAL_STATE_COMPONENT_TRACKING as ROLLENCE_KEY

open class InitialStateTracking(
    protected val iris: Iris,
) {

    companion object {

        object Category {
            const val INITIAL_STATE = "initial-state"
            const val TOP_NAV_INITIAL_STATE = "top nav - initial state"
        }

        object Action {
            const val IMPRESSION_RECENT_VIEW = "impression - recent view product"
            const val IMPRESSION_POPULAR_SEARCH = "impression - popular search"
            const val IMPRESSION_RECENT_SEARCH = "impression - recent search"
            const val IMPRESSION_SEE_MORE_RECENT_SEARCH = "impression see more - recent search"
            const val CLICK_RECENT_SEARCH = "click - recent search"
            const val CLICK_RECENT_SHOP = "click - shop - recent search"
            const val CLICK_SEE_MORE_RECENT_SEARCH = "click see more - recent search"
            const val CLICK_DYNAMIC_SECTION = "click - popular search"
            const val CLICK_CURATED_CAMPAIGN = "click - curated campaign"
            const val IMPRESSION_CURATED_CAMPAIGN = "impression - curated campaign"
            const val CLICK_RECENT_VIEW_PRODUCT = "click - recent view product"
            const val CLICK_INITIAL_STATE_PRODUCT_LINE = "click - product initial state campaign"
            const val CLICK_REFRESH_POPULAR_SEARCH = "click refresh on popular search"
            const val CLICK_REFRESH_TOKONOW_POPULAR_SEARCH =
                "click - refresh popular search - initial state"
            const val CLICK_POPULAR_SEARCH_TOKONOW = "click - popular search - initial state"
        }

        object Misc {
            const val HOMEPAGE = "homepage"
            const val RECENT_VIEW_ACTION_FIELD = "/search - recentview - product"
            const val PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD = "/search - product initial state campaign"
        }
    }

    open fun impressedRecentView(
        searchComponentTrackingList: List<SearchComponentTracking>,
        list: List<Any>
    ) {
        SearchComponentTrackingRollence.impress(iris, searchComponentTrackingList, ROLLENCE_KEY) {
            val map = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW_IRIS,
                EVENT_CATEGORY, "$TOP_NAV - $INITIAL_STATE",
                EVENT_ACTION, IMPRESSION_RECENT_VIEW,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(
                    CURRENCYCODE, IDR,
                    IMPRESSIONS, DataLayer.listOf(*list.toTypedArray())
                )
            )

            iris.saveEvent(map)
        }
    }

    open fun impressedRecentSearch(
        searchComponentTrackingList: List<SearchComponentTracking>,
        list: List<Any>,
        keyword: String,
    ) {
        SearchComponentTrackingRollence.impress(iris, searchComponentTrackingList, ROLLENCE_KEY) {
            val map = DataLayer.mapOf(
                EVENT, PROMO_VIEW_IRIS,
                EVENT_CATEGORY, "$TOP_NAV - $INITIAL_STATE",
                EVENT_ACTION, IMPRESSION_RECENT_SEARCH,
                EVENT_LABEL, keyword,
                ECOMMERCE, DataLayer.mapOf(
                    PROMOVIEW, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(*list.toTypedArray())
                    )
                )
            )

            iris.saveEvent(map)
        }
    }

    open fun impressedDynamicSection(
        searchComponentTrackingList: List<SearchComponentTracking>,
        model: DynamicInitialStateItemTrackingModel
    ) {
        SearchComponentTrackingRollence.impress(iris, searchComponentTrackingList, ROLLENCE_KEY) {
            val map = DataLayer.mapOf(
                EVENT, PROMO_VIEW_IRIS,
                EVENT_CATEGORY, "$TOP_NAV - /",
                EVENT_ACTION, IMPRESSION_POPULAR_SEARCH + " - " + model.type,
                EVENT_LABEL, "title: " + model.title,
                BUSINESS_UNIT, SEARCH,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, model.userId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMOVIEW, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(*model.list.toTypedArray())
                    )
                )
            )

            iris.saveEvent(map)
        }
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
        SearchComponentTrackingRollence.click(searchComponentTracking, ROLLENCE_KEY) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, CLICK_SEARCH,
                    EVENT_CATEGORY, TOP_NAV,
                    EVENT_ACTION, CLICK_RECENT_SEARCH,
                    EVENT_LABEL, label,
                    CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                    BUSINESS_UNIT, SEARCH,
                    PAGE_SOURCE, pageSource
                )
            )
        }
    }

    open fun eventClickRecentShop(
        searchComponentTracking: SearchComponentTracking,
        label: String,
        userId: String,
        pageSource: String
    ) {
        SearchComponentTrackingRollence.click(searchComponentTracking, ROLLENCE_KEY) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, CLICK_TOP_NAV,
                    EVENT_CATEGORY, TOP_NAV,
                    EVENT_ACTION, CLICK_RECENT_SHOP,
                    EVENT_LABEL, label,
                    SCREEN_NAME, "/",
                    CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                    USER_ID, userId,
                    BUSINESS_UNIT, SEARCH,
                    PAGE_SOURCE, pageSource
                )
            )
        }
    }

    open fun eventClickSeeMoreRecentSearch(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
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
        SearchComponentTrackingRollence.click(searchComponentTracking, ROLLENCE_KEY) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, CLICK_TOP_NAV,
                    EVENT_CATEGORY, TOP_NAV,
                    EVENT_ACTION, "$CLICK_DYNAMIC_SECTION - $type",
                    EVENT_LABEL, label,
                    BUSINESS_UNIT, SEARCH,
                    CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                    USER_ID, userId,
                    PAGE_SOURCE, pageSource
                )
            )
        }
    }

    open fun eventClickCuratedCampaignCard(
        searchComponentTracking: SearchComponentTracking,
        userId: String,
        label: String,
        type: String,
        campaignCode: String
    ) {
        SearchComponentTrackingRollence.click(searchComponentTracking, ROLLENCE_KEY) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, CLICK_TOP_NAV,
                    EVENT_CATEGORY, "$TOP_NAV_INITIAL_STATE - /",
                    EVENT_ACTION, "$CLICK_CURATED_CAMPAIGN - $type",
                    EVENT_LABEL, label,
                    CAMPAIGN_CODE, campaignCode,
                    BUSINESS_UNIT, SEARCH,
                    CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                    USER_ID, userId
                )
            )
        }
    }

    open fun impressedCuratedCampaign(
        searchComponentTracking: SearchComponentTracking,
        userId: String,
        label: String,
        type: String,
        campaignCode: String
    ) {
        SearchComponentTrackingRollence.impress(iris, listOf(searchComponentTracking), ROLLENCE_KEY) {
            val map = DataLayer.mapOf(
                EVENT, VIEW_TOP_NAV_IRIS,
                EVENT_CATEGORY, "$TOP_NAV_INITIAL_STATE - /",
                EVENT_ACTION, "$IMPRESSION_CURATED_CAMPAIGN - $type",
                EVENT_LABEL, label,
                CAMPAIGN_CODE, campaignCode,
                BUSINESS_UNIT, SEARCH,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
            )

            iris.saveEvent(map)
        }
    }

    open fun eventClickRecentView(productDataLayer: Any, label: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
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
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
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
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_TOP_NAV,
            "$TOP_NAV - $HOMEPAGE",
            CLICK_REFRESH_POPULAR_SEARCH,
            ""
        )
    }

    open fun eventClickRefreshTokoNowPopularSearch() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
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
        SearchComponentTrackingRollence.click(searchComponentTracking, ROLLENCE_KEY) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, CLICK_TOKO_NOW,
                    EVENT_CATEGORY, TOP_NAV_TOKO_NOW,
                    EVENT_ACTION, CLICK_POPULAR_SEARCH_TOKONOW,
                    EVENT_LABEL, label,
                    BUSINESS_UNIT, SEARCH,
                    CURRENT_SITE, TOKOPEDIA_MARKETPLACE
                )
            )
        }
    }

    open fun eventClickSearchBarEducation(item: BaseItemInitialStateSearch) {
        item.click(TrackApp.getInstance().gtm)
    }
}