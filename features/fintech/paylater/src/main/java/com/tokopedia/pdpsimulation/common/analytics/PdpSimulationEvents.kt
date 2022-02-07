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
}

open class PayLaterAnalyticsBase {
    var productId: String = ""
    var userStatus: String = ""
    var tenureOption: Int = 0
    var payLaterPartnerName: String = ""
    var action: String = ""
    var timeStamp: Long = System.currentTimeMillis()
}

open class PayLaterProductImpressionEvent: PayLaterAnalyticsBase() {
    var emiAmount: String = ""
}

@Parcelize
open class PayLaterBottomSheetImpression: PayLaterProductImpressionEvent(), Parcelable {
    var limit: String = ""
    var redirectLink: String = ""
}

@Parcelize
class PayLaterCtaClick: PayLaterBottomSheetImpression(), Parcelable {
    var ctaWording: String = ""
}