package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateProductParam(
        @SerializedName("productName")
        @Expose
        var productName: String? = null,
        @SerializedName("sku")
        @Expose
        var productSku: String? = null
)