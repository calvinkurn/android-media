package com.tokopedia.product.addedit.preview.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateProductNameParam(
        @SerializedName("productName")
        @Expose
        var productName: String? = null
)