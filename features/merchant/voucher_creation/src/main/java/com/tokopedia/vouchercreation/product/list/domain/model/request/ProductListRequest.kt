package com.tokopedia.vouchercreation.product.list.domain.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoodsFilterInput(
        @SerializedName("id")
        @Expose val id: String,
        @SerializedName("value")
        @Expose val value: List<String>
)

data class GoodsSortInput(
        @SerializedName("id")
        @Expose val id: String,
        @SerializedName("value")
        @Expose val value: String
)

data class GoodsSortExtraInput(
        @SerializedName("id")
        @Expose val id: String,
        @SerializedName("value")
        @Expose val value: String,
        @SerializedName("body")
        @Expose val body: List<String>
)