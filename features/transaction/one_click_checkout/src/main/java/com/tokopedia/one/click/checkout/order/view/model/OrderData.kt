package com.tokopedia.one.click.checkout.order.view.model

import com.tokopedia.one.click.checkout.order.data.OccMainOnboarding
import com.tokopedia.one.click.checkout.order.data.ProfileResponse

data class OrderData(
        var onboarding: OccMainOnboarding = OccMainOnboarding(),
        var cart: OrderCart = OrderCart(),
        var profileIndex: String = "",
        var profileRecommendation: String = "",
        var preference: ProfileResponse = ProfileResponse(),
        var promo: OrderPromo = OrderPromo()
)