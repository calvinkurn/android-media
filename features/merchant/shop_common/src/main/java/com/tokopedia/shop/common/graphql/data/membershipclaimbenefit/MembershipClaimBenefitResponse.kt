package com.tokopedia.shop.common.graphql.data.membershipclaimbenefit


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MembershipClaimBenefitResponse(
    @SerializedName("membershipClaimBenefit")
    @Expose
    val membershipClaimBenefitResponse: MembershipClaimData = MembershipClaimData()
)