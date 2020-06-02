package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.OccMainOnboarding
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse

data class OrderPreference(
        val onboarding: OccMainOnboarding = OccMainOnboarding(),
        val profileIndex: String? = null,
        val profileRecommendation: String? = null,
        val preference: ProfileResponse = ProfileResponse(),
        val shipping: Shipment? = null)