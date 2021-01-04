package com.tokopedia.product.addedit.description.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateProductDescriptionParam(
        @SerializedName("description")
        @Expose
        var description: String? = null
)