package com.tokopedia.shop.common.graphql.data.stampprogress


import com.google.gson.annotations.SerializedName

data class MembershipStampProgress(
    @SerializedName("membershipStampProgress")
    val membershipStampProgress: MembershipData = MembershipData()
)