package com.tokopedia.logisticcart.boaffordability.model

import com.google.gson.annotations.SerializedName

class BoAffordabilityDataResponse(
    @SerializedName("min_transaction")
    val minTransaction: Long = 0,
    @SerializedName("texts")
    val texts: BoAffordabilityTexts = BoAffordabilityTexts()
)
