package com.tokopedia.product.addedit.common.domain.model.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductAddEditV3Response(
    @SerializedName("ProductUpdateV3")
    @Expose
    val productAddEditV3Data: ProductAddEditV3Data = ProductAddEditV3Data()
)