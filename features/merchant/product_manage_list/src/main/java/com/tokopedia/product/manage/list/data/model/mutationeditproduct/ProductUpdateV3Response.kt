package com.tokopedia.product.manage.list.data.model.mutationeditproduct


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductUpdateV3Response(
    @SerializedName("ProductUpdateV3")
    @Expose
    val productUpdateV3Data: ProductUpdateV3Data = ProductUpdateV3Data()
)