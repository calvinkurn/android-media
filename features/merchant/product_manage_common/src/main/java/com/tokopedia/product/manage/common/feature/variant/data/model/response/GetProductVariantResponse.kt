package com.tokopedia.product.manage.common.feature.variant.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.common.feature.variant.data.model.GetProductV3

data class GetProductVariantResponse(
    @Expose
    @SerializedName("getProductV3")
    val getProductV3: GetProductV3
)