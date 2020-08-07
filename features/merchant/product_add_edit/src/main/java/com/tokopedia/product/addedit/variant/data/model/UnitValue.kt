package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UnitValue (
        @SerializedName("variantUnitValueID")
        @Expose
        var variantUnitValueID: Int = 0,
        @SerializedName("status")
        @Expose
        var status: Int = 0,
        @SerializedName("value")
        @Expose
        var value: String = "",
        @SerializedName("hex")
        @Expose
        var hex: String = "",
        @SerializedName("icon")
        @Expose
        var icon: String = ""
)
