package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateProductNameExistParam(
        @SerializedName("productName")
        @Expose
        val productName: String
)