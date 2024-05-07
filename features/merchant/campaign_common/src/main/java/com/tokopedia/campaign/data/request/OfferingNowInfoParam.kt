package com.tokopedia.campaign.data.request

import com.google.gson.annotations.SerializedName

data class OfferingNowInfoParam(
    @SerializedName("warehouses")
    val warehouses: List<WarehouseParam> = emptyList()
)
