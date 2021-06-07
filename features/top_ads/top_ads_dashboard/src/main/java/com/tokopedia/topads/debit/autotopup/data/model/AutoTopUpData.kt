package com.tokopedia.topads.debit.autotopup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class AutoTopUpData(
        @SerializedName("data")
        @Expose
        val data: AutoTopUpStatus = AutoTopUpStatus(),

        @Expose
        @SerializedName("errors")
        val errors: List<Error> = listOf()
){
    data class Response(
            @SerializedName("topAdsAutoTopupV2", alternate = ["topAdsPostAutoTopup"])
            @Expose
            val response: AutoTopUpData? = null
    )
}