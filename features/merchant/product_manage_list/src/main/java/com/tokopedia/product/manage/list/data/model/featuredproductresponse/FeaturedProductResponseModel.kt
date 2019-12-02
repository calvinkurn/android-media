package com.tokopedia.product.manage.list.data.model.featuredproductresponse

import com.google.gson.annotations.SerializedName

data class FeaturedProductResponseModel (
        @SerializedName("GoldManageFeaturedProductV2")
        var goldManageFeaturedProductV2: GoldManageFeaturedProductV2? = null
)

data class GoldManageFeaturedProductV2 (
        @SerializedName("header")
        var header: Header? = null
)

data class Header (
        @SerializedName("process_time")
        var processTime: Int = 0,
        @SerializedName("reason")
        var reason: String? = null,
        @SerializedName("error_code")
        var errorCode: String? = null,
        @SerializedName("message")
        var message: ArrayList<String>? = null
)
