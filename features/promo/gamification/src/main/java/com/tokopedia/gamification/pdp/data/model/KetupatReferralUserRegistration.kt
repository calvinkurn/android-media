package com.tokopedia.gamification.pdp.data.model

import com.google.gson.annotations.SerializedName


data class KetupatReferralUserRegistration (
    @SerializedName("name")
    var name : String?,
    @SerializedName("referralCode")
    var referralCode : String?,
    @SerializedName("tier")
    var tier : Tier?,
    @SerializedName("audienceGroupKey")
    var audienceGroupKey : String?,
    @SerializedName("isEligible")
    var isEligible : Boolean?,
    @SerializedName("isRequested")
    var isRequested : Boolean?
) {
    data class Tier(
        @SerializedName("tierID")
        var tierID: Long? = null,
        @SerializedName("title")
        var title: String? = null
    )
}
