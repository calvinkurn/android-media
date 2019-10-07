package com.tokopedia.shop.common.graphql.data.stampprogress


import com.google.gson.annotations.SerializedName

data class InfoMessage(
        @SerializedName("cta")
        val membershipCta: MembershipCta = MembershipCta(),

        @SerializedName("title")
        val title: String = ""
)