package com.tokopedia.editshipping.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateShippingParams(
        @SerializedName("shop_id")
        @Expose
        var shop_id: Int = -1,
        @SerializedName("shipment_ids")
        @Expose
        var shipments_ids: String = ""
)