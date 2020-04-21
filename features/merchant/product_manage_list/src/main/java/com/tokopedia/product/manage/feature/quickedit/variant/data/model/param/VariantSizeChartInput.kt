package com.tokopedia.product.manage.feature.quickedit.variant.data.model.param

import com.google.gson.annotations.SerializedName

data class VariantSizeChartInput(
    @SerializedName("picID")
    val picId: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("filePath")
    val filePath: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)