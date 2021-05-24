package com.tokopedia.logisticorder.domain.response

import com.google.gson.annotations.SerializedName

data class GetDeliveryImageResponse(
        @SerializedName("image")
        val image: String
)