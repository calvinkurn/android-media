package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.data.get.ProfileResponse

data class OrderPreference(
        val onboarding: OccMainOnboarding = OccMainOnboarding(),
        val profileIndex: String? = null,
        val profileRecommendation: String? = null,
        val preference: ProfileResponse = ProfileResponse(),
        val shipping: OrderShipment? = null
)