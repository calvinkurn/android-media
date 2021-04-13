package com.tokopedia.power_merchant.subscribe.view_old.model

data class PowerMerchantFreeShippingStatus(
    val isActive: Boolean,
    val isEligible: Boolean,
    val isTransitionPeriod: Boolean,
    val isPowerMerchantIdle: Boolean,
    val isPowerMerchantActive: Boolean
)