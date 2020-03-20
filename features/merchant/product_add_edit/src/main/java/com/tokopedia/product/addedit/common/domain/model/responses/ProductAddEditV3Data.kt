package com.tokopedia.product.addedit.common.domain.model.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductAddEditV3Data(
        @SerializedName("header")
        @Expose
        val header: ProductAddEditV3Header = ProductAddEditV3Header(),
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Boolean = false,

        var productId: String = ""
)