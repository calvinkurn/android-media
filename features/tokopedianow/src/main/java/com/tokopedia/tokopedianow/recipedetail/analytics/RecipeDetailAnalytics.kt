package com.tokopedia.tokopedianow.recipedetail.analytics

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
import com.tokopedia.tokopedianow.recipecommon.analytics.RecipeCommonAnalyticsConstant.EVENT_CATEGORY_TOKONOW_RECIPE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.USERID
import com.tokopedia.user.session.UserSessionInterface

/**
 * Docs:
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3155
 */
class RecipeDetailAnalytics(private val userSession: UserSessionInterface) {

    companion object {
        private const val EVENT_ACTION_CLICK_BOOKMARK = "click bookmark"
        private const val EVENT_ACTION_CLICK_SHARE = "click share"
        private const val EVENT_ACTION_IMPRESSION_OTHER_TAGS = "impression other tags"
        private const val EVENT_ACTION_CLICK_OTHER_TAGS = "click other tags"
        private const val EVENT_ACTION_IMPRESSION_BUY_INGREDIENTS_TAB = "impression belanja bahan tab"
        private const val EVENT_ACTION_CLICK_BUY_INGREDIENTS_TAB = "click belanja bahan tab"
        private const val EVENT_ACTION_VIEW_HOW_TO_TAB = "view cara masak tab"
        private const val EVENT_ACTION_CLICK_HOW_TO_TAB = "click cara masak tab"
        private const val EVENT_ACTION_IMPRESSION_OUT_OF_COVERAGE = "impression ooc"
        private const val EVENT_ACTION_CLICK_CHANGE_ADDRESS = "click ganti alamat"
        private const val EVENT_ACTION_CLICK_LEARN_MORE = "click pelajari"
        private const val EVENT_ACTION_CLICK_ATC_TOASTER = "click lihat atc toaster"
    }

    fun trackClickBookmark() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_BOOKMARK
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackClickShare() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_SHARE
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionOtherTags() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_OTHER_TAGS
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickOtherTags() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_OTHER_TAGS
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionBuyIngredientsTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_BUY_INGREDIENTS_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickBuyIngredientsTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_BUY_INGREDIENTS_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionHowToTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_VIEW_HOW_TO_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickHowToTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_HOW_TO_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionOutOfCoverage() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_OUT_OF_COVERAGE
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickChangeAddress() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_CHANGE_ADDRESS
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackClickLearnMore() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_LEARN_MORE
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackClickSeeAddToCartToaster() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_ATC_TOASTER
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    private fun createGeneralDataLayer(event: String, action: String): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_RECIPE)
            putString(EVENT_LABEL, DEFAULT_EMPTY_VALUE)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(USERID, userSession.userId)
        }
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, dataLayer: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, dataLayer)
    }
}