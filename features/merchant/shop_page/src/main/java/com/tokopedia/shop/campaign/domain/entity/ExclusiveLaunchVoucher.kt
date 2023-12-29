package com.tokopedia.shop.campaign.domain.entity

import com.tokopedia.kotlin.model.ImpressHolder

data class ExclusiveLaunchVoucher(
    val id: Long,
    val voucherName: String,
    val minimumPurchase: Long,
    val remainingQuota: Int,
    val slug: String,
    val isDisabledButton: Boolean,
    val couponCode: String,
    val buttonStr: String
) : ImpressHolder()
