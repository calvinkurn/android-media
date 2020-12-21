package com.tokopedia.product.manage.common.feature.quickedit.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductUpdateV3Response(
        @SerializedName("ProductUpdateV3")
        @Expose
        val productUpdateV3Data: ProductUpdateV3Data = ProductUpdateV3Data()
)