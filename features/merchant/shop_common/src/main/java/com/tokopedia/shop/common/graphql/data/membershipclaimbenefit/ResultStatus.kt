package com.tokopedia.shop.common.graphql.data.membershipclaimbenefit


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultStatus(
        @SerializedName("code")
        @Expose
        val code: String = "",

        @SerializedName("message")
        @Expose
        val message: List<String> = listOf(),

        @SerializedName("reason")
        @Expose
        val reason: String = ""
)