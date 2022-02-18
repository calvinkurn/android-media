package com.tokopedia.pdp.fintech.analytics

sealed class FintechWidgetAnalyticsEvent {
    data class PdpWidgetImpression(
        val productId: String,
        val userStatus: String,
        val chipType: String,
        val partnerId: String
    ) : FintechWidgetAnalyticsEvent()

    data class ActivationBottomSheetClick(
        val userStatus: String,
        val partner: String,
        val ctaWording: String
    ) : FintechWidgetAnalyticsEvent()
}