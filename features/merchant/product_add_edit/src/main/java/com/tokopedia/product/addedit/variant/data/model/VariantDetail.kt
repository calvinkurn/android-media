package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO
import java.math.BigInteger

data class VariantDetail (
        @SerializedName("VariantID")
        @Expose
        var variantID: BigInteger = Int.ZERO.toBigInteger(),
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
        var units: List<Unit> = emptyList(),
        @SerializedName("isCustom")
        @Expose
        var isCustom: Boolean = false
)
