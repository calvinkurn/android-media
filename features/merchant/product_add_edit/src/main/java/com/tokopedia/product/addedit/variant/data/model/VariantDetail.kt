package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VariantDetail (
        @SerializedName("VariantID")
        @Expose
        var variantID: Int = 0,
        @SerializedName("Identifier")
        @Expose
        var identifier: String = "",
        @SerializedName("Name")
        @Expose
        var name: String = "",
        @SerializedName("Status")
        @Expose
        var status: Int = 0,
        @SerializedName("Units")
        @Expose
        var units: List<Unit> = emptyList()
)
