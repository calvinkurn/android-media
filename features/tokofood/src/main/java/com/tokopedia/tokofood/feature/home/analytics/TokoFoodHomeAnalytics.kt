package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Home
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053
 */
class TokoFoodHomeAnalytics: BaseTrackerConst() {

    fun clickLCAWidget(userId: String?, destinationId: String?) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_LCA)
            putString(TrackAppUtils.EVENT, "")
        }
        eventDataLayer.clickPG(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.CLICK_PG, eventDataLayer)
    }

    fun Bundle.clickPG(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.CLICK_PG)
        return this
    }

    fun Bundle.addGeneralTracker(userId: String?, destinationId: String?): Bundle {
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
        this.putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
        this.putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
        this.putString(TokoFoodAnalyticsConstants.USER_ID, userId ?: "")
        this.putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: "")
        return this
    }
}