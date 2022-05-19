package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetVariantDataByIdResponse (
    @SerializedName("getVariantDataByID")
    @Expose
    val getVariantDataByID: GetVariantDataById
)

data class GetVariantDataById (
    @SerializedName("data")
    @Expose
    val variantDetail: VariantDetail
)