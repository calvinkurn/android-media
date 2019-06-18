package com.tokopedia.transactiondata.insurance.entity.response

import com.google.gson.annotations.SerializedName

data class InsuranceCartGqlResponse(
        @SerializedName("data")
        val data: String = ""
)