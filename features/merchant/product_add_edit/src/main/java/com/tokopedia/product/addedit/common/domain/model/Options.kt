package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Options (
    
    @SerializedName("unitValueID")
    @Expose
    var unitValueID: String? = null,
    @SerializedName("value")
    @Expose
    var value: String? = null,
    @SerializedName("hexCode")
    @Expose
    var hexCode: String? = null

)
