package com.tokopedia.product.manage.common.feature.variant.data.model.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VariantSizeChartInput(
    @Expose
    @SerializedName("picID")
    val picId: String,
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("filePath")
    val filePath: String,
    @Expose
    @SerializedName("fileName")
    val fileName: String,
    @Expose
    @SerializedName("width")
    val width: Int,
    @Expose
    @SerializedName("height")
    val height: Int
)