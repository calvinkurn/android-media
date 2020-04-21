package com.tokopedia.product.manage.feature.multiedit.data.response

import com.google.gson.annotations.SerializedName

data class MultiEditProduct(
    @SerializedName("BulkProductEditV3")
    val results: List<MultiEditProductResult>? = null
)