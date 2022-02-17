package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAddOnData {
    @SerializedName("GetAddOnByProductResponse")
    @Expose
    var getAddOnByProductResponse: GetAddOnByProductResponse? = null
}