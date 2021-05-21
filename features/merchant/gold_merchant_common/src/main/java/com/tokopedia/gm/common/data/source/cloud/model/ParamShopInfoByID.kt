package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParamShopInfoByID(
        @Expose
        @SerializedName("shopIDs")
        val shopIDs: List<Int> = listOf(),
        @Expose
        @SerializedName("fields")
        val fields: List<String> = listOf("create_info"),
        @Expose
        @SerializedName("domain")
        val domain: String = "",
        @Expose
        @SerializedName("source")
        val source: String = "sellerapp",
)