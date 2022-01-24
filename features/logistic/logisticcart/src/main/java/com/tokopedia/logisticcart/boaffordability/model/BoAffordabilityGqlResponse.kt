package com.tokopedia.logisticcart.boaffordability.model

import com.google.gson.annotations.SerializedName

class BoAffordabilityGqlResponse(
        @SerializedName("freeshipping")
        val response: BoAffordabilityResponse = BoAffordabilityResponse()
)