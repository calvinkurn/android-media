package com.tokopedia.shop.flashsale.domain.entity

import android.os.Parcelable
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import kotlinx.parcelize.Parcelize

data class MerchantCampaignTNC(
    val title: String = "",
    val messages: List<String> = listOf(),
    val error: Error = Error()
) {
    data class Error(
        val errorCode: Int = 0,
        val errorMessage: String = "",
    )

    @Parcelize
    data class TncRequest(
        var campaignId: Long = 0,
        var isUniqueBuyer: Boolean = false,
        var isCampaignRelation: Boolean = false,
        var paymentType: PaymentType = PaymentType.INSTANT
    ): Parcelable
}

