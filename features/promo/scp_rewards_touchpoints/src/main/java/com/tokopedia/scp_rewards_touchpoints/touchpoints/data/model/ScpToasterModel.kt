package com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model

import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse

data class AnalyticsData(
    val orderId: String = "",
    val pagePath: String = "",
    val pageType: String = ""
)

data class ScpToasterModel(
    val analyticsData: AnalyticsData = AnalyticsData(),
    val responseData: ScpRewardsMedalTouchPointResponse
)
