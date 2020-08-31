package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Unit (
        @SerializedName("variantUnitID")
        @Expose
        var variantUnitID: Int = 0,
        @SerializedName("status")
        @Expose
        var status: Int = 0,
        @SerializedName("unitName")
        @Expose
        var unitName: String = "",
        @SerializedName("unitShortName")
        @Expose
        var unitShortName: String = "",
        @SerializedName("unitValues")
        @Expose
        var unitValues: MutableList<UnitValue> = mutableListOf()
)
