package com.tokopedia.autocompletecomponent.suggestion.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.ACTION_FIELD
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.BUSINESS_UNIT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CLICK
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.CURRENT_SITE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Category.TOP_NAV
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.ECOMMERCE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_ACTION
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_CATEGORY
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.EVENT_LABEL
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.CLICK_TOP_NAV
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.Event.PRODUCT_CLICK
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.LIST
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.PRODUCTS
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.SEARCH
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.autocompletecomponent.analytics.AutoCompleteTrackingConstant.USER_ID
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking.Companion.Action.CLICK_SUGGESTION_PRODUCT_LINE
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking.Companion.Action.CLICK_TOP_SHOP
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking.Companion.Action.CLICK_TOP_SHOP_SEE_MORE
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking.Companion.Misc.PRODUCT_LINE_SUGGESTION_ACTION_FIELD
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingRollence
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

open class SuggestionTracking(
    protected val iris: Iris,
) {

    companion object {

        object Action {
            const val CLICK_KEYWORD_SUGGESTION = "click - product autocomplete"
            const val CLICK_DIGITAL_PRODUCT_SUGGESTION = "click - digital product suggestion"
            const val CLICK_SHOP_SUGGESTION = "click - shop autocomplete"
            const val CLICK_PROFILE_SUGGESTION = "click - profile autocomplete on suggestion list"
            const val CLICK_RECENT_SEARCH_AUTOCOMPLETE = "click - recent search autocomplete"
            const val CLICK_TOP_SHOP = "click - shop - carousel"
            const val CLICK_TOP_SHOP_SEE_MORE = "click - lihat toko lainnya - carousel"
            const val CLICK_LOCAL_KEYWORD = "click - autocomplete local"
            const val CLICK_GLOBAL_KEYWORD = "click - autocomplete global"
            const val CLICK_SUGGESTION_PRODUCT_LINE = "click - product autocomplete campaign"
            const val CLICK_TOKONOW_CURATED_SUGGESTION = "click - campaign autocomplete - autocomplete"
            const val CLICK_TOKONOW_KEYWORD_SUGGESTION = "click - product autocomplete - autocomplete"
            const val CLICK_CHIP_SUGGESTION = "click - related autocomplete"
            const val IMPRESSION_DIGITAL_CURATED_SUGGESTION = "impression - digital product suggestion"
        }

        object Misc {
            const val PRODUCT_LINE_SUGGESTION_ACTION_FIELD = "/search - product autocomplete campaign"
        }
    }

    private val analytics: Analytics
        get() = TrackApp.getInstance().gtm

    open fun eventClickKeyword(
        label: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickCurated(
        label: String,
        campaignCode: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickShop(
        label: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickProfile(label: String, searchComponentTracking: SearchComponentTracking) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickRecentKeyword(
        keyword: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickTopShop(label: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, CLICK_TOP_NAV,
                EVENT_CATEGORY, "$TOP_NAV - /",
                EVENT_ACTION, CLICK_TOP_SHOP,
                EVENT_LABEL, label
            )
        )
    }

    open fun eventClickTopShopSeeMore(label: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, CLICK_TOP_NAV,
                EVENT_CATEGORY, "$TOP_NAV - /",
                EVENT_ACTION, CLICK_TOP_SHOP_SEE_MORE,
                EVENT_LABEL, label
            )
        )
    }

    open fun eventClickLocalKeyword(
        label: String,
        userId: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickGlobalKeyword(
        label: String,
        userId: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }


    open fun eventClickSuggestionProductLine(productDataLayer: Any, label: String, userId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, TOP_NAV,
                EVENT_ACTION, CLICK_SUGGESTION_PRODUCT_LINE,
                EVENT_LABEL, label,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, PRODUCT_LINE_SUGGESTION_ACTION_FIELD),
                        PRODUCTS, DataLayer.listOf(productDataLayer)
                    )
                ),
                BUSINESS_UNIT, SEARCH,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
            )
        )
    }

    open fun eventClickTokoNowCurated(
        label: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickTokoNowKeyword(
        label: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(analytics)
    }

    open fun eventClickChipSuggestion(
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.click(TrackApp.getInstance().gtm)
    }

    open fun eventImpressCurated(
        label: String,
        campaignCode: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking,
    ) {
        searchComponentTracking.impress(iris)
    }

    open fun eventImpressionSuggestion(searchComponentTracking: SearchComponentTracking) {
        searchComponentTracking.impress(iris)
    }

    open fun eventClickSuggestion(searchComponentTracking: SearchComponentTracking) {
        searchComponentTracking.click(analytics)
    }
}
