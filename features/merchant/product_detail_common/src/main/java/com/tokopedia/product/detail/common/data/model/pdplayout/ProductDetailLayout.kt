package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class ProductDetailLayout(
        @SerializedName("pdpGetLayout")
        val data: PdpGetLayout = PdpGetLayout()
)