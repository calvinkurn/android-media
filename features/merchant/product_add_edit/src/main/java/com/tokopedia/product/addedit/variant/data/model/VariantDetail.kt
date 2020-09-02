package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VariantDetail (
        @SerializedName("variantID")
        @Expose
        var variantID: Int = 0,
        @SerializedName("identifier")
        @Expose
        var identifier: String = "",
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("status")
        @Expose
        var status: Int = 0,
        @SerializedName("units")
        @Expose
        var units: List<Unit> = emptyList()
)
