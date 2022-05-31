package com.tokopedia.product.addedit.preview.data.model.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductAddEditV3Data(
        @SerializedName("header")
        @Expose
        val header: ProductAddEditV3Header = ProductAddEditV3Header(),
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Boolean = false,
        @SerializedName("productID")
        @Expose
        var productId: String = ""
)