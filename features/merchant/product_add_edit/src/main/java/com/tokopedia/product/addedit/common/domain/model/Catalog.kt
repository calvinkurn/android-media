package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Catalog (

    @SerializedName("catalogID")
    @Expose
    var catalogID: String? = null

)
