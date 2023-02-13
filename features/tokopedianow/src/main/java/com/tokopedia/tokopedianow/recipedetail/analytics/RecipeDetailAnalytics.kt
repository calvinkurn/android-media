package com.tokopedia.tokopedianow.recipedetail.analytics

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
import com.tokopedia.tokopedianow.recipecommon.analytics.RecipeCommonAnalyticsConstant.EVENT_CATEGORY_TOKONOW_RECIPE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_ATC_TOASTER
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_BOOKMARK
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_BUY_INGREDIENTS_TAB
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_CHANGE_ADDRESS
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_HOW_TO_TAB
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_LEARN_MORE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_OTHER_TAGS
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_CLICK_SHARE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_IMPRESSION_BUY_INGREDIENTS_TAB
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_IMPRESSION_OTHER_TAGS
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_IMPRESSION_OUT_OF_COVERAGE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.ACTION.EVENT_ACTION_VIEW_HOW_TO_TAB
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_ATC_TOASTER
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_BOOKMARK
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_BUY_INGREDIENTS_TAB
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_CHANGE_ADDRESS
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_HOW_TO_TAB
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_LEARN_MORE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_OTHER_TAGS
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_CLICK_SHARE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_BUY_INGREDIENTS_TAB
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_OTHER_TAGS
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_OUT_OF_COVERAGE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeDetailAnalytics.TRACKER_ID.TRACKER_ID_VIEW_HOW_TO_TAB
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

    private object ACTION {
        const val EVENT_ACTION_CLICK_BOOKMARK = "click bookmark"
        const val EVENT_ACTION_CLICK_SHARE = "click share"
        const val EVENT_ACTION_IMPRESSION_OTHER_TAGS = "impression other tags"
        const val EVENT_ACTION_CLICK_OTHER_TAGS = "click other tags"
        const val EVENT_ACTION_IMPRESSION_BUY_INGREDIENTS_TAB = "impression belanja bahan tab"
        const val EVENT_ACTION_CLICK_BUY_INGREDIENTS_TAB = "click belanja bahan tab"
        const val EVENT_ACTION_VIEW_HOW_TO_TAB = "view cara masak tab"
        const val EVENT_ACTION_CLICK_HOW_TO_TAB = "click cara masak tab"
        const val EVENT_ACTION_IMPRESSION_OUT_OF_COVERAGE = "impression ooc"
        const val EVENT_ACTION_CLICK_CHANGE_ADDRESS = "click ganti alamat"
        const val EVENT_ACTION_CLICK_LEARN_MORE = "click pelajari"
        const val EVENT_ACTION_CLICK_ATC_TOASTER = "click lihat atc toaster"
    }

    private object TRACKER_ID {
        const val TRACKER_ID_CLICK_BOOKMARK = "32997"
        const val TRACKER_ID_CLICK_SHARE = "32998"
        const val TRACKER_ID_IMPRESSION_OTHER_TAGS = "32999"
        const val TRACKER_ID_CLICK_OTHER_TAGS = "33000"
        const val TRACKER_ID_IMPRESSION_BUY_INGREDIENTS_TAB = "33001"
        const val TRACKER_ID_CLICK_BUY_INGREDIENTS_TAB = "33035"
        const val TRACKER_ID_VIEW_HOW_TO_TAB = "33036"
        const val TRACKER_ID_CLICK_HOW_TO_TAB = "33037"
        const val TRACKER_ID_IMPRESSION_OUT_OF_COVERAGE = "33058"
        const val TRACKER_ID_CLICK_CHANGE_ADDRESS = "33059"
        const val TRACKER_ID_CLICK_LEARN_MORE = "33060"
        const val TRACKER_ID_CLICK_ATC_TOASTER = "33053"
    }

    fun trackClickBookmark() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_BOOKMARK,
            trackerId = TRACKER_ID_CLICK_BOOKMARK
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackClickShare() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_SHARE,
            trackerId = TRACKER_ID_CLICK_SHARE
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionOtherTags() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_OTHER_TAGS,
            trackerId = TRACKER_ID_IMPRESSION_OTHER_TAGS
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickOtherTags() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_OTHER_TAGS,
            trackerId = TRACKER_ID_CLICK_OTHER_TAGS
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionBuyIngredientsTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_BUY_INGREDIENTS_TAB,
            trackerId = TRACKER_ID_IMPRESSION_BUY_INGREDIENTS_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickBuyIngredientsTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_BUY_INGREDIENTS_TAB,
            trackerId = TRACKER_ID_CLICK_BUY_INGREDIENTS_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionHowToTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_VIEW_HOW_TO_TAB,
            trackerId = TRACKER_ID_VIEW_HOW_TO_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickHowToTab() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_HOW_TO_TAB,
            trackerId = TRACKER_ID_CLICK_HOW_TO_TAB
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionOutOfCoverage() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_OUT_OF_COVERAGE,
            trackerId = TRACKER_ID_IMPRESSION_OUT_OF_COVERAGE
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickChangeAddress() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_CHANGE_ADDRESS,
            trackerId = TRACKER_ID_CLICK_CHANGE_ADDRESS
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackClickLearnMore() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_LEARN_MORE,
            trackerId = TRACKER_ID_CLICK_LEARN_MORE
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    fun trackClickSeeAddToCartToaster() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_ATC_TOASTER,
            trackerId = TRACKER_ID_CLICK_ATC_TOASTER
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    private fun createGeneralDataLayer(event: String, action: String, trackerId: String): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_RECIPE)
            putString(EVENT_LABEL, DEFAULT_EMPTY_VALUE)
            putString(KEY_TRACKER_ID, trackerId)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(USERID, userSession.userId)
        }
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, dataLayer: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, dataLayer)
    }
}
