package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BoAdditionalData(
    val code: String = "",
    val uniqueId: String = "",
    val cartStringGroup: String = "",
    val shippingId: Long = 0,
    val spId: Long = 0,
    val promoId: Long = 0,
    val shippingPrice: Double = 0.0,
    val shippingSubsidy: Long = 0,
    val benefitClass: String = "",
    val boCampaignId: Long = 0,
    val etaText: String = ""
) : Parcelable
