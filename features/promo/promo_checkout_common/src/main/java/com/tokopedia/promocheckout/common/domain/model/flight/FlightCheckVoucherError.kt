package com.tokopedia.promocheckout.common.domain.model.flight

import com.google.gson.annotations.SerializedName

data class FlightCheckVoucherError(
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("title")
    val title: String
)
