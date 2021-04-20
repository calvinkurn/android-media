package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetVariantCategoryCombinationResponse (
        @SerializedName("getVariantCategoryCombination")
        @Expose
        var getVariantCategoryCombination: GetVariantCategoryCombination = GetVariantCategoryCombination()
)

data class GetVariantCategoryCombination (
        @SerializedName("header")
        @Expose
        var header: Header = Header(),
        @SerializedName("data")
        @Expose
        var data: Data = Data()
)