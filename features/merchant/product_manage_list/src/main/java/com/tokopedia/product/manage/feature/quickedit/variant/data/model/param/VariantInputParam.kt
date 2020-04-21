package com.tokopedia.product.manage.feature.quickedit.variant.data.model.param

import com.google.gson.annotations.SerializedName

data class VariantInputParam(
    @SerializedName("products")
    val products: List<VariantProductInput>,
    @SerializedName("selections")
    val selections: List<VariantSelectionInput>,
    @SerializedName("sizeChart")
    val sizeCharts: List<VariantSizeChartInput>
)