package com.tokopedia.universal_sharing.view.model

import com.google.gson.annotations.SerializedName


data class AffiliateEligibility (
    @SerializedName("IsRegistered")
    var isRegistered: Boolean = false,

    @SerializedName("IsEligible")
    var isEligible: Boolean = false
)

data class EligibleCommission (
    @SerializedName("IsEligible")
    var isEligible: Boolean = false,

    @SerializedName("AmountFormatted")
    var amountFormatted: String? = "",

    @SerializedName("Amount")
    var amount: String? = "",

    @SerializedName("Message")
    var message: String? = ""
)

data class Banner (
    @SerializedName("Icon")
    val icon: String = "",

    @SerializedName("Title")
    val title: String = "",

    @SerializedName("Message")
    val message: String = "",

    @SerializedName("CtaLink")
    val ctaLink: String = ""
)

data class GenerateAffiliateLinkEligibility(
    @SerializedName("Status")
    var status: Int = 0,

    @SerializedName("ShowTicker")
    var showTicker:Boolean = false,

    @SerializedName("TickerType")
    var tickerType:Int = 0,

    @SerializedName("Message")
    var message: String? = "",

    @SerializedName("AffiliateEligibility")
    var affiliateEligibility: AffiliateEligibility? = null,

    @SerializedName("Banner")
    var banner: Banner? = null,

    @SerializedName("EligibleCommission")
    var eligibleCommission: EligibleCommission? = null
){
    data class Response(
        @SerializedName("generateAffiliateLinkEligibility")
        val generateAffiliateLinkEligibility: GenerateAffiliateLinkEligibility
    )
}