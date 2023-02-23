package com.tokopedia.tokopedianow.recipedetail.analytics

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.MediaSliderAnalytics
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PROMO_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PROMO_VIEW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
import com.tokopedia.tokopedianow.recipecommon.analytics.RecipeCommonAnalyticsConstant.EVENT_CATEGORY_TOKONOW_RECIPE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.ACTION.EVENT_ACTION_CLICK_FULLSCREEN
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.ACTION.EVENT_ACTION_IMAGE_CLICK
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.ACTION.EVENT_ACTION_IMAGE_IMPRESSION
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.ACTION.EVENT_ACTION_VIDEO_CLICK
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.ACTION.EVENT_ACTION_VIDEO_IMPRESSION
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.TRACKER_ID.TRACKER_ID_CLICK_FULLSCREEN
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.TRACKER_ID.TRACKER_ID_IMAGE_CLICK
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.TRACKER_ID.TRACKER_ID_IMAGE_IMPRESSION
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.TRACKER_ID.TRACKER_ID_VIDEO_CLICK
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.TRACKER_ID.TRACKER_ID_VIDEO_IMPRESSION
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeMediaSliderAnalytics.VALUE.PROMOTIONS_SLIDER_BANNER_ITEM_NAME
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
 * Documentations:
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3155
 */

class RecipeMediaSliderAnalytics(
    private val userSession: UserSessionInterface
) : MediaSliderAnalytics {

    private object ACTION {
        const val EVENT_ACTION_IMAGE_IMPRESSION = "slider banner image impression"
        const val EVENT_ACTION_IMAGE_CLICK = "slider banner image click"
        const val EVENT_ACTION_VIDEO_IMPRESSION = "slider banner video impression"
        const val EVENT_ACTION_VIDEO_CLICK = "slider banner video click"
        const val EVENT_ACTION_CLICK_FULLSCREEN = "click fullscreen"
    }

    private object VALUE {
        const val PROMOTIONS_SLIDER_BANNER_ITEM_NAME = "/ - p%d - slider banner - banner - "
    }

    private object TRACKER_ID {
        const val TRACKER_ID_IMAGE_IMPRESSION = "32992"
        const val TRACKER_ID_IMAGE_CLICK = "32993"
        const val TRACKER_ID_VIDEO_IMPRESSION = "32994"
        const val TRACKER_ID_VIDEO_CLICK = "32995"
        const val TRACKER_ID_CLICK_FULLSCREEN = "32996"
    }

    override fun trackImageImpression(position: Int) {
        val promotions = createPromotionsDataLayer(position)

        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMAGE_IMPRESSION,
            trackerId = TRACKER_ID_IMAGE_IMPRESSION
        )

        dataLayer.putParcelableArrayList(KEY_PROMOTIONS, promotions)

        sendEnhanceEcommerceEvent(
            eventName = EVENT_PROMO_VIEW,
            dataLayer = dataLayer
        )
    }

    override fun trackClickImage(position: Int) {
        val promotions = createPromotionsDataLayer(position)

        val dataLayer = createGeneralDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_IMAGE_CLICK,
            trackerId = TRACKER_ID_IMAGE_CLICK
        )

        dataLayer.putParcelableArrayList(KEY_PROMOTIONS, promotions)

        sendEnhanceEcommerceEvent(
            eventName = EVENT_PROMO_CLICK,
            dataLayer = dataLayer
        )
    }

    override fun trackVideoImpression(position: Int) {
        val promotions = createPromotionsDataLayer(position)

        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_VIDEO_IMPRESSION,
            trackerId = TRACKER_ID_VIDEO_IMPRESSION
        )

        dataLayer.putParcelableArrayList(KEY_PROMOTIONS, promotions)

        sendEnhanceEcommerceEvent(
            eventName = EVENT_PROMO_VIEW,
            dataLayer = dataLayer
        )
    }

    override fun trackClickVideo(position: Int) {
        val promotions = createPromotionsDataLayer(position)

        val dataLayer = createGeneralDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_VIDEO_CLICK,
            trackerId = TRACKER_ID_VIDEO_CLICK
        )

        dataLayer.putParcelableArrayList(KEY_PROMOTIONS, promotions)

        sendEnhanceEcommerceEvent(
            eventName = EVENT_PROMO_CLICK,
            dataLayer = dataLayer
        )
    }

    override fun trackClickFullscreen() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_FULLSCREEN,
            trackerId = TRACKER_ID_CLICK_FULLSCREEN
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    private fun createPromotionsDataLayer(position: Int) = arrayListOf(
        Bundle().apply {
            putString(KEY_CREATIVE_NAME, DEFAULT_EMPTY_VALUE)
            putString(KEY_CREATIVE_SLOT, DEFAULT_EMPTY_VALUE)
            putString(KEY_ITEM_ID, DEFAULT_EMPTY_VALUE)
            putString(KEY_ITEM_NAME, PROMOTIONS_SLIDER_BANNER_ITEM_NAME.format(position))
        }
    )

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
