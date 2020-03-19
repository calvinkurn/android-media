package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Variant (
    
    @SerializedName("sizeChart")
    @Expose
    var sizeChart: Pictures? = null,
    @SerializedName("products")
    @Expose
    var products: Products? = null,
    @SerializedName("selections")
    @Expose
    var selections: Selections? = null

)
