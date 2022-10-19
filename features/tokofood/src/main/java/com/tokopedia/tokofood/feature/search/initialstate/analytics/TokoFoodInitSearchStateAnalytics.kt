package com.tokopedia.tokofood.feature.search.initialstate.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoFoodInitSearchStateAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    private val tracker by lazy { TrackApp.getInstance().gtm }

    fun impressViewSearchHistory(
        keyword: String, destinationId: String, position: Int
    ) {

        val eventBundle = Bundle().appendGeneralEventData(
            eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
            eventAction = TokoFoodAnalyticsConstants.VIEW_SEARCH_HISTORY_TOKOFOOD,
            eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            eventLabel = keyword
        ).appendCustomDimensions(
            trackerId = TokoFoodAnalyticsConstants.TRACKER_ID_35767,
            userId = userSession.userId,
            destinationId = destinationId,
            pageSource = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION
        ).appendPromotions(
            position = position,
            itemId = keyword,
            itemName = "${TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION} - $position"
        )
        eventBundle.sendEnhancedEcommerce(TokoFoodAnalyticsConstants.VIEW_ITEM)
    }

    fun clickSearchHistory(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_SEARCH_HISTORY_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35768,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.PAGE_SOURCE to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION
        )
        tracker.sendGeneralEvent(eventData)
    }

    fun impressViewTopKeyword(keyword: String, destinationId: String, position: Int) {
        val eventBundle = Bundle().appendGeneralEventData(
            eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
            eventAction = TokoFoodAnalyticsConstants.VIEW_TOP_KEYWORD_TOKOFOOD,
            eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            eventLabel = String.EMPTY
        ).appendCustomDimensions(
            trackerId = TokoFoodAnalyticsConstants.TRACKER_ID_35769,
            userId = userSession.userId,
            destinationId = destinationId,
            pageSource = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION
        ).appendPromotions(
            position = position,
            itemId = keyword,
            itemName = null
        )
        eventBundle.sendEnhancedEcommerce(TokoFoodAnalyticsConstants.VIEW_ITEM)
    }

    fun clickTopKeyword(keyword: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_TOP_KEYWORD_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35770,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.PAGE_SOURCE to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION
        )
        tracker.sendGeneralEvent(eventData)
    }

    fun impressViewCuisineItem(
        keyword: String,
        destinationId: String,
        position: Int,
        cuisineName: String
    ) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.VIEW_PG_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_CUISINE_LIST_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to keyword,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35771,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        tracker.sendGeneralEvent(eventData)

        val eventBundle = Bundle().appendGeneralEventData(
            eventName = TokoFoodAnalyticsConstants.VIEW_ITEM,
            eventAction = TokoFoodAnalyticsConstants.VIEW_CUISINE_LIST_TOKOFOOD,
            eventCategory = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            eventLabel = keyword
        ).appendCustomDimensions(
            trackerId = TokoFoodAnalyticsConstants.TRACKER_ID_35771,
            userId = userSession.userId,
            destinationId = destinationId,
            pageSource = TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION
        ).appendPromotions(
            position = position,
            itemId = cuisineName,
            itemName = null
        )
        eventBundle.sendEnhancedEcommerce(TokoFoodAnalyticsConstants.VIEW_ITEM)
    }

    fun clickCuisineList(keyword: String, cuisineName: String) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_CUISINE_LIST_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TrackAppUtils.EVENT_LABEL to "$keyword - $cuisineName",
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_35772,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.PAGE_SOURCE to TokoFoodAnalyticsConstants.TOKOFOOD_SEARCH_RECOMMENDATION,
            TokoFoodAnalyticsConstants.USER_ID to userSession.userId
        )
        tracker.sendGeneralEvent(eventData)
    }

    private fun Bundle.appendGeneralEventData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ): Bundle {
        putString(TrackAppUtils.EVENT, eventName)
        putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
        putString(TrackAppUtils.EVENT_ACTION, eventAction)
        putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        return this
    }

    private fun Bundle.appendCustomDimensions(
        trackerId: String,
        userId: String,
        destinationId: String,
        pageSource: String
    ): Bundle {
        putString(TokoFoodAnalyticsConstants.TRACKER_ID, trackerId)
        putString(
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT,
            TokoFoodAnalyticsConstants.PHYSICAL_GOODS
        )
        putString(
            TokoFoodAnalyticsConstants.CURRENT_SITE,
            TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
        )
        putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId)
        putString(TokoFoodAnalyticsConstants.USER_ID, userId)
        putString(TokoFoodAnalyticsConstants.PAGE_SOURCE, pageSource)
        return this
    }

    private fun Bundle.appendPromotions(position: Int, itemId: String, itemName: String?): Bundle {
        val promotionsPayload = listOf(Bundle().apply {
            putString(TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_NAME, "null")
            putInt(TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_SLOT, position)
            putString(TokoFoodAnalyticsConstants.KEY_EE_ITEM_ID, itemId)
            putString(TokoFoodAnalyticsConstants.KEY_EE_ITEM_NAME, "$itemName")
        })

        putParcelableArrayList(
            TokoFoodAnalyticsConstants.KEY_EE_PROMOTIONS,
            ArrayList(promotionsPayload)
        )

        return this
    }

    private fun Bundle.sendEnhancedEcommerce(eventName: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, this)
    }
}
