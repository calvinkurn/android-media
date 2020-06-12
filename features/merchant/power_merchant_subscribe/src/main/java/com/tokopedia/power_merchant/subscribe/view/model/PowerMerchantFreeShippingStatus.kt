package com.tokopedia.power_merchant.subscribe.view.model

data class PowerMerchantFreeShippingStatus(
    val isActive: Boolean,
    val isEligible: Boolean,
    val isTransitionPeriod: Boolean,
    val isShopScoreEligible: Boolean
)