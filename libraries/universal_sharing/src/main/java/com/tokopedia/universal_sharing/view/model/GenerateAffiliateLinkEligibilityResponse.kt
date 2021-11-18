package com.tokopedia.universal_sharing.view.model

import com.google.gson.annotations.SerializedName


class AffiliateEligibility {
    @SerializedName("IsRegistered")
    var isRegistered = false

    @SerializedName("IsEligible")
    var isEligible = false
}

class EligibleCommission {
    @SerializedName("IsEligible")
    var isEligible = false

    @SerializedName("AmountFormatted")
    var amountFormatted: String? = ""

    @SerializedName("Amount")
    var amount: String? = ""

    @SerializedName("Message")
    var message: String? = ""
}

class GenerateAffiliateLinkEligibility {
    @SerializedName("Status")
    var status = 0

    @SerializedName("ShowTicker")
    var showTicker = false

    @SerializedName("TickerType")
    var tickerType = 0

    @SerializedName("Message")
    var message: String? = ""

    @SerializedName("AffiliateEligibility")
    var affiliateEligibility: AffiliateEligibility? = null

    @SerializedName("EligibleCommission")
    var eligibleCommission: EligibleCommission? = null
}