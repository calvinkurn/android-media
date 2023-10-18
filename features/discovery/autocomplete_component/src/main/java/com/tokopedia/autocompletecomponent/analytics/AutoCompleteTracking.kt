package com.tokopedia.autocompletecomponent.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTracking.Action.CLICK_CARI
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTracking.Action.CLICK_SEARCH_BAR
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTracking.Action.CLICK_SEARCH_SEARCH_BAR
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTracking.Category.LONG_PRESS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTracking.Label.PRODUCT_SEARCH
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.BUSINESS_UNIT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CURRENT_SITE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Category.TOP_NAV_TOKO_NOW
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_ACTION
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_CATEGORY
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_LABEL
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_SEARCH
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_TOKO_NOW
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.LONG_CLICK
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PAGE_SOURCE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.USER_ID
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Category.SEARCH_COMPONENT
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Component.AUTO_COMPLETE_CANCEL_SEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Component.AUTO_COMPLETE_MANUAL_ENTER
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Component.AUTO_COMPLETE_VOICE_SEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Component.INITIAL_STATE_CANCEL_SEARCH
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst.Component.INITIAL_STATE_MANUAL_ENTER
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface

open class AutoCompleteTracking(
    protected val userSession: UserSessionInterface,
    protected val iris: Iris,
) {

    object Category {
        const val LONG_PRESS = "Long Press"
    }

    object Action {
        const val CLICK_CARI = "Click Cari"
        const val CLICK_SEARCH_SEARCH_BAR = "click - search - search bar"
        const val CLICK_SEARCH_BAR = "click search bar"
    }

    object Label {
        const val PRODUCT_SEARCH = "Product Search"
    }

    private val analytics: Analytics
        get() = TrackApp.getInstance().gtm

    open fun eventInitiateSearchSession(dimension90: String) {
        analytics.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_ACTION, CLICK_SEARCH_BAR,
                EVENT_CATEGORY, SEARCH_COMPONENT,
                EVENT_LABEL, "",
                BUSINESS_UNIT, AutoCompleteTrackingConstant.SEARCH,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                PAGE_SOURCE, dimension90,
            )
        )
    }

    open fun eventSearchShortcut() {
        analytics.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, LONG_CLICK,
                EVENT_CATEGORY, LONG_PRESS,
                EVENT_ACTION, CLICK_CARI,
                EVENT_LABEL, PRODUCT_SEARCH,
                USER_ID, if (userSession.isLoggedIn) userSession.userId else "0"
            )
        )
    }

    open fun eventClickSubmitInitialState(
        keyword: String,
        pageSource: String,
        searchResultApplink: String,
    ) {
        eventClickSearchComponentTracking(
            keyword, pageSource, searchResultApplink, INITIAL_STATE_MANUAL_ENTER
        )
    }

    private fun eventClickSearchComponentTracking(
        keyword: String,
        pageSource: String,
        searchResultApplink: String,
        componentId: String,
    ) {
        searchComponentTracking(
            keyword = keyword,
            componentId = componentId,
            dimension90 = pageSource,
            applink = searchResultApplink,
        ).click(analytics)
    }

    open fun eventClickSubmitAutoComplete(
        keyword: String,
        pageSource: String,
        searchResultApplink: String
    ) {
        eventClickSearchComponentTracking(
            keyword, pageSource, searchResultApplink, AUTO_COMPLETE_MANUAL_ENTER
        )
    }

    open fun eventClickSubmitTokoNow(label: String) {
        analytics.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_TOKO_NOW,
                EVENT_CATEGORY, TOP_NAV_TOKO_NOW,
                EVENT_ACTION, CLICK_SEARCH_SEARCH_BAR,
                EVENT_LABEL, label,
                BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            )
        )
    }

    open fun eventClickDiscoveryVoiceSearch(label: String, pageSource: String) {
        searchComponentTracking(
            keyword = label,
            componentId = AUTO_COMPLETE_VOICE_SEARCH,
            dimension90 = pageSource,
        ).click(analytics)
    }

    open fun eventImpressDiscoveryVoiceSearch(pageSource: String) {
        searchComponentTracking(
            componentId = AUTO_COMPLETE_VOICE_SEARCH,
            dimension90 = pageSource,
        ).impress(iris)
    }

    open fun eventCancelSearch(query: String, pageSource: String) {
        val componentId = getCancelSearchComponentId(query)

        searchComponentTracking(
            keyword = query,
            componentId = componentId,
            dimension90 = pageSource,
        ).clickOtherAction(analytics)
    }

    private fun getCancelSearchComponentId(query: String) =
        if (query.isEmpty()) INITIAL_STATE_CANCEL_SEARCH
        else AUTO_COMPLETE_CANCEL_SEARCH
}
