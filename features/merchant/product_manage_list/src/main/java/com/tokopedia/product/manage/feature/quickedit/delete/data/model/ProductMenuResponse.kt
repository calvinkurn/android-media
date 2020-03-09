package com.tokopedia.product.manage.feature.quickedit.delete.data.model

import com.google.gson.annotations.SerializedName

data class ProductMenuResponse(
        @SerializedName("header")
        val header: DeleteProductHeader,
        @SerializedName("isSuccess")
        val isSuccess: Boolean
)