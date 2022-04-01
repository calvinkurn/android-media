package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data (
        @SerializedName("variantIDCombinations")
        @Expose
        var variantIDCombinations: List<List<Int>> = emptyList(),

        @SerializedName("variantDetails")
        @Expose
        var variantDetails: List<VariantDetail> = emptyList(),

        @SerializedName("allVariants")
        @Expose
        var allVariants: List<AllVariant> = emptyList()
)
