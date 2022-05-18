package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class GetProductV3Response(
        @SerializedName("getProductV3")
        val product: Product
)