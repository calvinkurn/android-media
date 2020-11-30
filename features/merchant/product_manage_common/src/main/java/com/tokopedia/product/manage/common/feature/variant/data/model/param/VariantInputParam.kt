package com.tokopedia.product.manage.common.feature.variant.data.model.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VariantInputParam(
        @Expose
    @SerializedName("products")
    val products: List<VariantProductInput>,
        @Expose
    @SerializedName("selections")
    val selections: List<VariantSelectionInput>,
        @Expose
    @SerializedName("sizeChart")
    val sizeCharts: List<VariantSizeChartInput>
)