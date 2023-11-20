package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.gifting.domain.model.StaticInfo


data class GetAddOnByProduct (
    @SerializedName("Error")
    var error: Error = Error(),

    @SerializedName("AddOnByProductResponse")
    var addOnByProductResponse: List<AddOnByProductResponse> = listOf(),

    @SerializedName("StaticInfo")
    var staticInfo: StaticInfo = StaticInfo()
)
