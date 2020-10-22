package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UnitValue (
        @SerializedName("VariantUnitValueID")
        @Expose
        var variantUnitValueID: Int = 0,
        @SerializedName("Status")
        @Expose
        var status: Int = 0,
        @SerializedName("Value")
        @Expose
        var value: String = "",
        @SerializedName("Hex")
        @Expose
        var hex: String = "",
        @SerializedName("Icon")
        @Expose
        var icon: String = ""
)
