package com.tokopedia.pdpsimulation.common.analytics

open class PayLaterAnalyticsBase {
    var productId: String = ""
    var userStatus: String = ""
    var tenureOption: Int = 0
    var payLaterPartnerName: String = ""
    var linkingStatus: String = ""
    var action: String = ""
}

class PayLaterCtaClick : PayLaterAnalyticsBase() {
    var emiAmount: String = ""
    var limit: String = ""
    var redirectLink: String = ""
    var ctaWording: String = ""
}