package com.tokopedia.logisticcart.boaffordability.model

import com.google.gson.annotations.SerializedName

class BoAffordabilityGqlResponse(
        @SerializedName("free_shipping")
        val response: BoAffordabilityResponse = BoAffordabilityResponse()
)