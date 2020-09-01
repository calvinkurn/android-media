package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding

data class OrderData(
        var onboarding: OccMainOnboarding = OccMainOnboarding(),
        var cart: OrderCart = OrderCart(),
        var profileIndex: String = "",
        var profileRecommendation: String = "",
        var preference: OrderProfile = OrderProfile(),
        var promo: OrderPromo = OrderPromo(),
        var payment: OrderPayment = OrderPayment()
)