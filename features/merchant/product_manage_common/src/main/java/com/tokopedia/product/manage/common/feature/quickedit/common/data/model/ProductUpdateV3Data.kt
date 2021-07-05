package com.tokopedia.product.manage.common.feature.quickedit.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductUpdateV3Data(
        @SerializedName("header")
        @Expose
        val header: ProductUpdateV3Header = ProductUpdateV3Header(),
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Boolean = false
)