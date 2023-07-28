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
    ) : PdpSimulationEvent()

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

    data class ClickCTACheckoutPage(
        val productId: String,
        val userStatus: String,
        val partnerName: String,
        val emiAmount: String,
        val tenure: String,
        val quantity: String,
        val limit: String,
        val variantName: String,
        val promoName: String,
        val productPrice: String,
        val prevRate: String,
        val newRate: String
    ) : PdpSimulationEvent()

    data class ClickTenureEvent(
        val productId: String,
        val userStatus: String,
        val productPrice: String,
        val tenure: String,
        val partnerName: String,
        val promoName: String,
        val prevRate: String,
        val newRate: String
    ) : PdpSimulationEvent()
}

open class PayLaterAnalyticsBase {
    var productId: String = ""
    var userStatus: String = ""
    var tenureOption: Int = 0
    var payLaterPartnerName: String = ""
    var linkingStatus: String = ""
    var action: String = ""
    var promoName: String = ""
    var previousRate: String = ""
    var newRate: String = ""
}

class PayLaterCtaClick : PayLaterAnalyticsBase() {
    var emiAmount: String = ""
    var limit: String = ""
    var redirectLink: String = ""
    var ctaWording: String = ""
}

class PayLaterTickerCtaClick : PayLaterAnalyticsBase() {
    var tickerType: String = ""
    var tickerCta: String = ""
}

class PayLaterTickerImpression : PayLaterAnalyticsBase() {
    var tickerType: String = ""
}

@Parcelize
class PayLaterBottomSheetImpression : PayLaterAnalyticsBase(), Parcelable {
    var limit: String = ""
    var emiAmount: String = ""
}

@Parcelize
class OccBottomSheetImpression : PayLaterAnalyticsBase(), Parcelable {
    var productPrice: String = ""
}

class PayLaterTenureClick : PayLaterAnalyticsBase() {
    var productPrice: String = ""
}
