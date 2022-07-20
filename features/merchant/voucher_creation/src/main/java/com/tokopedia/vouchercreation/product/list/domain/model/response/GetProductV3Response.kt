package com.tokopedia.vouchercreation.product.list.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetProductV3Response(
        @SerializedName("getProductV3")
        @Expose val product: Product
)