package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Wholesale (
    
    @SerializedName("minQty")
    @Expose
    var minQty: Int? = null,
    @SerializedName("price")
    @Expose
    var price: Int? = null

)
