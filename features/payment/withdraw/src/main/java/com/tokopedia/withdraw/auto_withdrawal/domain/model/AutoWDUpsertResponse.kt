package com.tokopedia.withdraw.auto_withdrawal.domain.model

import com.google.gson.annotations.SerializedName

data class AutoWDUpsertResponse (
    @SerializedName("UpsertAutoWDData")
    val upsertResponse : UpsertResponse
)

data class UpsertResponse (
        @SerializedName("code")
        val code : Int,
        @SerializedName("message")
        val message : String
)
