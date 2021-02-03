package com.tokopedia.checkout.data.model.response.egold

import com.google.gson.annotations.SerializedName

data class EgoldAttributes(
    @SerializedName("eligible")
    var isEligible: Boolean = false,
    @SerializedName("is_tiering")
    var isTiering: Boolean = false,
    @SerializedName("is_opt_in")
    val isOptIn: Boolean = false,
    @SerializedName("range")
    var egoldRange: EgoldRange = EgoldRange(),
    @SerializedName("message")
    var egoldMessage: EgoldMessage = EgoldMessage(),
    @SerializedName("tier_data")
    var egoldTieringDataArrayList: List<EgoldTieringData> = emptyList()
)