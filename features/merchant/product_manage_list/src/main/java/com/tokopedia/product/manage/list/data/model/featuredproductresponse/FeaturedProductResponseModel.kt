package com.tokopedia.product.manage.list.data.model.featuredproductresponse

import com.google.gson.annotations.SerializedName

data class FeaturedProductResponseModel (
        @SerializedName("GoldManageFeaturedProductV2")
        val goldManageFeaturedProductV2: GoldManageFeaturedProductV2? = null
)

data class GoldManageFeaturedProductV2 (
        @SerializedName("header")
        val header: Header? = null
)

data class Header (
        @SerializedName("error_code")
        val errorCode: String = "",
        @SerializedName("message")
        val message: List<String> = listOf()
)
