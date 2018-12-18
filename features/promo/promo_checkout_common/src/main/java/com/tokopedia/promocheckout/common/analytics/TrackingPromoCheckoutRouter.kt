package com.tokopedia.promocheckout.common.analytics

interface TrackingPromoCheckoutRouter {
    fun sendEventTracking(event: String, category: String, action: String, label: String)
}
