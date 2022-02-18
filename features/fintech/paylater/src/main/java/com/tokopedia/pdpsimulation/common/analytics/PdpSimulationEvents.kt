package com.tokopedia.pdpsimulation.common.analytics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class PdpSimulationEvent {

    data class OccImpressionEvent(
        val productId: String,
        val userStatus: String,
        val partnerName: String,
        val emiAmount: String,
        val tenure: String,
        val quantity: String,
        val limit: String,
        val variantName: String
    ) : PdpSimulationEvent()

    data class OccChangeVariantClicked(
        val productId: String,
        val userStatus: String,
        val partnerName: String,
        val emiAmount: String,
        val tenure: String,
        val quantity: String,
        val limit: String,
        val variantName: String
    ) : PdpSimulationEvent()

    data class OccChangePartnerClicked(
        val productId: String,
        val userStatus: String,
        val partnerName: String,
        val emiAmount: String,
        val tenure: String,
        val quantity: String,
        val limit: String,
        val variantName: String
    ): PdpSimulationEvent()



    data class ClickChangePartnerEvent(
        val productId: String,
        val userStatus: String,
        val partnerName: String,
        val emiAmount: String,
        val tenure: String,
        val quantity: String,
        val limit: String,
        val variantName: String
    ) : PdpSimulationEvent()
}

open class PayLaterAnalyticsBase {
    var productId: String = ""
    var userStatus: String = ""
    var tenureOption: Int = 0
    var payLaterPartnerName: String = ""
    var linkingStatus: String = ""
    var action: String = ""
}

class PayLaterCtaClick: PayLaterAnalyticsBase(){
    var emiAmount: String = ""
    var limit: String = ""
    var redirectLink: String = ""
    var ctaWording: String = ""
}