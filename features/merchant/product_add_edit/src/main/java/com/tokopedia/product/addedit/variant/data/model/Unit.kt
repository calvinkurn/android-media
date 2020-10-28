package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Unit (
        @SerializedName("VariantUnitID")
        @Expose
        var variantUnitID: Int = 0,
        @SerializedName("Status")
        @Expose
        var status: Int = 0,
        @SerializedName("UnitName")
        @Expose
        var unitName: String = "",
        @SerializedName("UnitShortName")
        @Expose
        var unitShortName: String = "",
        @SerializedName("UnitValues")
        @Expose
        var unitValues: MutableList<UnitValue> = mutableListOf()
)
