package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.data.get.ProfileResponse

data class OrderData(
        var onboarding: OccMainOnboarding = OccMainOnboarding(),
        var cart: OrderCart = OrderCart(),
        var profileIndex: String = "",
        var profileRecommendation: String = "",
        var preference: ProfileResponse = ProfileResponse(),
        var promo: OrderPromo = OrderPromo()
)