package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductLabel(
    @SerializedName("label_type")
    val labelType: String = "",
    @SerializedName("label_detail")
    val labelDetail: LabelData = LabelData()
)
