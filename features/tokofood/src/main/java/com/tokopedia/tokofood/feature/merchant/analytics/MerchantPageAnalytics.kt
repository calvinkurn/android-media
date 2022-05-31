package com.tokopedia.tokofood.feature.merchant.analytics

import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/2988
 */

class MerchantPageAnalytics @Inject constructor() {

    private val tracker by lazy { TrackApp.getInstance().gtm }

    fun impressShareBottomSheet(itemId: String, userId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_ON_SHARING_CHANNEL,
            TrackAppUtils.EVENT_CATEGORY to ShareComponentConstants.TOKOFOOD,
            TrackAppUtils.EVENT_LABEL to "${TokoFoodAnalyticsConstants.MERCHANT} \n $itemId",
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.USER_ID to String.format(TokoFoodAnalyticsConstants.USER_ID_FORMAT, userId)
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun closeShareBottomSheet(itemId: String, userId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            TrackAppUtils.EVENT_CATEGORY to ShareComponentConstants.TOKOFOOD,
            TrackAppUtils.EVENT_LABEL to "${TokoFoodAnalyticsConstants.MERCHANT} \n $itemId",
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.USER_ID to String.format(TokoFoodAnalyticsConstants.USER_ID_FORMAT, userId)
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickShareBottomSheet(itemId: String, userId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_SHARE_BOTTOM_SHEET,
            TrackAppUtils.EVENT_CATEGORY to ShareComponentConstants.TOKOFOOD,
            TrackAppUtils.EVENT_LABEL to "${TokoFoodAnalyticsConstants.MERCHANT} \n $itemId",
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.USER_ID to String.format(TokoFoodAnalyticsConstants.USER_ID_FORMAT, userId)
        )
        tracker.sendGeneralEvent(mapData)
    }
}