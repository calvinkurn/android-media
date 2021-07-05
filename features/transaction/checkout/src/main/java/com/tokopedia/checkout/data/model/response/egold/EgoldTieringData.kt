package com.tokopedia.checkout.data.model.response.egold

import com.google.gson.annotations.SerializedName

data class EgoldTieringData (
    @SerializedName("minimum_total_amount")
    var minTotalAmount: Long = 0,
    @SerializedName("minimum_amount")
    var minAmount: Long = 0,
    @SerializedName("maximum_amount")
    var maxAmount: Long = 0,
    @SerializedName("basis_amount")
    var basisAmount: Long = 0
)