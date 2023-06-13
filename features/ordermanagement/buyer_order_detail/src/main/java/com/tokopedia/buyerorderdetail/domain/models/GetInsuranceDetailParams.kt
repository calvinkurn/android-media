package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetInsuranceDetailParams(
    @SerializedName("invoice")
    @Expose
    val invoice: String,
    @Expose(serialize = false, deserialize = false)
    @Transient
    val shouldCheckCache: Boolean
)
