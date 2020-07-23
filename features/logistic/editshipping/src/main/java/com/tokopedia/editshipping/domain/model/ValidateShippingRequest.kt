package com.tokopedia.editshipping.domain.model

import com.google.gson.annotations.SerializedName

data class ValidateShippingRequest(
        @SerializedName("shop_id")
        val shopId: Int = -1,
        @SerializedName("shipment_ids")
        val shipmentId: String = ""
)