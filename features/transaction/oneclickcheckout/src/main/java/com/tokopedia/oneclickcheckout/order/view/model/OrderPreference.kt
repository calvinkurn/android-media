package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding

data class OrderPreference(
        val onboarding: OccMainOnboarding = OccMainOnboarding(),
        val profileIndex: String? = null,
        val profileRecommendation: String? = null,
        val preference: OrderProfile = OrderProfile(),
        val isValid: Boolean = false
)