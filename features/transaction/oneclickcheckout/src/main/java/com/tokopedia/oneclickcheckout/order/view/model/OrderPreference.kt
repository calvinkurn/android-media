package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.data.ProfileResponse

data class OrderPreference(
        val onboarding: OccMainOnboarding = OccMainOnboarding(),
        val profileIndex: String? = null,
        val profileRecommendation: String? = null,
        val preference: ProfileResponse = ProfileResponse(),
        val shipping: Shipment? = null)