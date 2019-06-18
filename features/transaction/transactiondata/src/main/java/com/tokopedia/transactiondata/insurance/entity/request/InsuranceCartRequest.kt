package com.tokopedia.transactiondata.insurance.entity.request

import com.google.gson.annotations.SerializedName

data class InsuranceCartRequest(

        @SerializedName("client_version")
        var clientVersion: String = "",

        @SerializedName("client_type")
        var clientType: String = "android",

        @SerializedName("client_lang")
        var clientLang: String = "en"
)
