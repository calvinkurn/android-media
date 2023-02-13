package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class LayoutTopAdsAttr(
        @SerializedName("jsonTopAdsDisplayParam")
        var jsonTopAdsDisplayParam: String = ""
)
