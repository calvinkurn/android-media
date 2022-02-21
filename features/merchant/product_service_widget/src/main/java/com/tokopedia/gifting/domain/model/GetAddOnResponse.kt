package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAddOnResponse {
    @SerializedName("data")
    @Expose
    var data: GetAddOnData = GetAddOnData()
}