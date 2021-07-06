package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.common.data.model.OrderItem
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

data class OrderPreference(
        val ticker: TickerData? = null,
        val onboarding: OccMainOnboarding = OccMainOnboarding(),
        val preference: OrderProfile = OrderProfile(),
        val isValid: Boolean = false,
): OrderItem