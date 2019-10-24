package com.tokopedia.shop.common.graphql.data.stampprogress


import com.google.gson.annotations.SerializedName

data class MembershipCta(
        @SerializedName("appLink")
        val appLink: String = "",

        @SerializedName("text")
        val text: String = "",

        @SerializedName("url")
        val url: String = ""
)