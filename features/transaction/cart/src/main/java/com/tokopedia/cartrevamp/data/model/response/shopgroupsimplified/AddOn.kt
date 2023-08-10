package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class AddOn(
    @SerializedName("data")
    val addOnData: List<AddOnData> = emptyList(),
    @SerializedName("widget")
    val addOnWidget: AddOnWidget = AddOnWidget()
)
