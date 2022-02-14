package com.tokopedia.pdpsimulation.common.analytics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class PdpSimulationEvent {
    sealed class PayLater {

        data class GopayBottomSheetImpression(
            val productId: String,
            val tenure: String,
            val partnerName: String,
            val emiAmount: String
        ) : PdpSimulationEvent()

        data class GopayBottomSheetButtonClick(
            val productId: String,
            val tenure: String,
            val partnerName: String,
            val emiAmount: String,
            val url: String
        ) : PdpSimulationEvent()
    }

    data class OccImpressionEvent(
        val partnerName: String, val quantity: String, val emiAmount: String,
        val tenure: String, val limit: String, val variant: String, val userStatus: String
    ) : PdpSimulationEvent()

    data class OccChangeVariantClicked(
        val partnerName: String, val quantity: String, val emiAmount: String,
        val tenure: String, val limit: String, val variant: String, val userStatus: String
    ) : PdpSimulationEvent()

    data class OccProceedToCheckout(
        val partnerName: String, val quantity: String, val emiAmount: String,
        val tenure: String, val limit: String, val variant: String, val userStatus: String
    ) : PdpSimulationEvent()

    data class OccChangeVariantListener(val partnerName: String, val variant: String) :
        PdpSimulationEvent()

    data class ChangePartnerImperssion(
        val partnerName: String,
        val limit: String,
        val quantity: String,
        val variant: String,
        val userStatus: String
    ) : PdpSimulationEvent()

    data class ClickChangePartnerEvent(
        val partnerName: String,
        val limit: String,
        val quantity: String,
        val variant: String,
        val userStatus: String
    ) : PdpSimulationEvent()
}

open class PayLaterAnalyticsBase {
    var productId: String = ""
    var userStatus: String = ""
    var tenureOption: Int = 0
    var payLaterPartnerName: String = ""
    var action: String = ""
}

open class PayLaterProductImpressionEvent : PayLaterAnalyticsBase() {
    var emiAmount: String = ""
}

@Parcelize
open class PayLaterBottomSheetImpression : PayLaterProductImpressionEvent(), Parcelable {
    var limit: String = ""
    var redirectLink: String = ""
}

@Parcelize
class PayLaterCtaClick : PayLaterBottomSheetImpression(), Parcelable {
    var ctaWording: String = ""
}