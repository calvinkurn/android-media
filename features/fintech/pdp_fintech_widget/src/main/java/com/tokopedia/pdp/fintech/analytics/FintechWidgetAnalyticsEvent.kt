package com.tokopedia.pdp.fintech.analytics

sealed class FintechWidgetAnalyticsEvent {
    data class PdpWidgetImpression(
        val productId: String,
        val linkingStatus: String,
        val userStatus: String,
        val chipType: String,
        val partnerId: String,
        val promoName: String?,
        val subtitle: String?,
    ) : FintechWidgetAnalyticsEvent()

    data class ActivationBottomSheetClick(
        val userStatus: String,
        val linkingStatus: String,
        val partner: String,
        val ctaWording: String
    ) : FintechWidgetAnalyticsEvent()

    data class PdpWidgetClick(
        val productId: String?,
        val linkingStatus: String?,
        val userStatus: String?,
        val chipType: String?,
        val installmentAmount: String?,
        val tenure: String?,
        val partner: String?,
        val redirectionUrl: String?,
        val promoName: String?,
        val subtitle: String,
    ): FintechWidgetAnalyticsEvent()

    data class BnplWidgetClick(
        val usecaseRank: String,
        val linkingStatus: String,
        val userState: String,
        val widgetType: String,
        val productId: String,
        val productPrice: String,
        val installmentAmt: String,
        val partner: String,
    ): FintechWidgetAnalyticsEvent()

    data class BnplWidgetImpression(
        val usecaseRank: String,
        val linkingStatus: String,
        val userState: String,
        val widgetType: String,
        val productId: String,
        val productPrice: String,
        val installmentAmt: String,
        val partner: String,
    ): FintechWidgetAnalyticsEvent()
}
