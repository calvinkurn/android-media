package com.tokopedia.homenav.mainnav.data.pojo.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Metadata(
        @SerializedName("detailURL")
        @Expose
        val detailURL: DetailURL,
        @SerializedName("products")
        @Expose
        val products: List<Product>,
        @SerializedName("status")
        @Expose
        val status: Status
)