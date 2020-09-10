package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

data class OrderData(
        val ticker: TickerData? = null,
        var onboarding: OccMainOnboarding = OccMainOnboarding(),
        var cart: OrderCart = OrderCart(),
        var profileIndex: String = "",
        var profileRecommendation: String = "",
        var preference: OrderProfile = OrderProfile(),
        var promo: OrderPromo = OrderPromo(),
        var payment: OrderPayment = OrderPayment()
)