package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAddOnData {
    @SerializedName("GetAddOnByProduct")
    @Expose
    var getAddOnByProduct: GetAddOnByProduct? = null
}