package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Preorder (

    
    @SerializedName("duration")
    @Expose
    var duration: Int? = null,
    @SerializedName("timeUnit")
    @Expose
    var timeUnit: String? = null,
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean? = null

)
