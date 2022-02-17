package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAddOnByProductResponse {
    @SerializedName("error")
    @Expose
    var error: Error? = null

    @SerializedName("StaticInfo")
    @Expose
    var staticInfo: StaticInfo? = null

    @SerializedName("AddOnByProductResponse")
    @Expose
    var addOnByProductResponse: List<AddOnByProductResponse>? = null
}