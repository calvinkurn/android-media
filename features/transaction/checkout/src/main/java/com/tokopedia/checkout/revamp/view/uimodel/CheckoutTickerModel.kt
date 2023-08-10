package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData

data class CheckoutTickerModel(
    override val cartStringGroup: String = "",
    val ticker: TickerAnnouncementHolderData
) : CheckoutItem
