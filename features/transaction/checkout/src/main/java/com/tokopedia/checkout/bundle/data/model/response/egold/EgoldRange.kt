package com.tokopedia.checkout.bundle.data.model.response.egold

import com.google.gson.annotations.SerializedName

data class EgoldRange (
    @SerializedName("min")
    var minEgoldValue: Int = 0,
    @SerializedName("max")
    var maxEgoldValue: Int = 0
)