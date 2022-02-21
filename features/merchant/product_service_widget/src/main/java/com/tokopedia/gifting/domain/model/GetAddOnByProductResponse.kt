package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAddOnByProduct {
    @SerializedName("error")
    @Expose
    var error: Error = Error()

    @SerializedName("StaticInfo")
    @Expose
    var staticInfo: StaticInfo? = null

    @SerializedName("AddOnByProductResponse")
    @Expose
    var addOnByProductResponse: List<AddOnByProductResponse>? = null
}