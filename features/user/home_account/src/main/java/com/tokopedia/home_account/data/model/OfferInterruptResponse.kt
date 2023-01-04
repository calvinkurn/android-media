package com.tokopedia.home_account.data.model

import com.google.gson.annotations.SerializedName

data class OfferInterruptResponse(
    @SerializedName("offer_interrupt")
    val data: OfferInterruptData = OfferInterruptData()
)

data class OfferInterruptData(
    @SerializedName("errorMessage")
    var errorMessages: List<String> = listOf(),
    @SerializedName("offers")
    var offers: MutableList<OfferingData> = mutableListOf(),
    @SerializedName("interval")
    var interval: Int = 0
)

data class OfferingData(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("enableSkip")
    var enableSkip: Boolean = false
)
