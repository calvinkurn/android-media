package com.tokopedia.logisticcart.boaffordability.model

import com.google.gson.annotations.SerializedName

class BoAffordabilityResponse(
        @SerializedName("freeshipping")
        val data: BoAffordabilityDataResponse = BoAffordabilityDataResponse()
)