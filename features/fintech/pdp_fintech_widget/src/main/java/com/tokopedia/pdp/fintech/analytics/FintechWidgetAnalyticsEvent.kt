package com.tokopedia.pdp.fintech.analytics

sealed class FintechWidgetAnalyticsEvent{
    data class PdpWidgetImression(val productId: String,val userStatus:String,val partnerId:String):FintechWidgetAnalyticsEvent()
}