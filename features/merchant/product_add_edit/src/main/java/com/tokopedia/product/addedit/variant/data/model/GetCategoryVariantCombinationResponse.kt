package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetCategoryVariantCombinationResponse (
        @SerializedName("GetCategoryVariantCombination")
        @Expose
        var getCategoryVariantCombination: GetCategoryVariantCombination = GetCategoryVariantCombination()
)

data class GetCategoryVariantCombination (
        @SerializedName("header")
        @Expose
        var header: Header = Header(),
        @SerializedName("data")
        @Expose
        var data: Data = Data()
)