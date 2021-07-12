package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.common.data.model.OrderItem
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

data class OrderPreference(
        val ticker: TickerData? = null,
        val onboarding: OccOnboarding = OccOnboarding(),
        val hasValidProfile: Boolean = false
): OrderItem