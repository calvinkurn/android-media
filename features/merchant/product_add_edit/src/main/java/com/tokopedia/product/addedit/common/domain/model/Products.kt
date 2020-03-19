package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Products (
    
    @SerializedName("combination")
    @Expose
    var combination: List<Int>? = null,
    @SerializedName("isPrimary")
    @Expose
    var isPrimary: Boolean? = null,
    @SerializedName("price")
    @Expose
    var price: Int? = null,
    @SerializedName("sku")
    @Expose
    var sku: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("stock")
    @Expose
    var stock: Int? = null,
    @SerializedName("pictures")
    @Expose
    var pictures: Pictures? = null

)
