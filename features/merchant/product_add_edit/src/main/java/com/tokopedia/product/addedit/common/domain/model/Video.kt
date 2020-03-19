package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Video (
    
    @SerializedName("source")
    @Expose
    var source: String? = null,
    @SerializedName("url")
    @Expose
    var url: String? = null

)
