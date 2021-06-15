package com.tokopedia.shop.common.remoteconfig.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopAbTestInputParamModel(
        @SerializedName("listExpName")
        @Expose
        val listExpName: List<String> = listOf(),
        @SerializedName("ID")
        @Expose
        val id: String = "",
        @SerializedName("clientType")
        @Expose
        val clientType: String = "",
        @SerializedName("irisSessionId")
        @Expose
        var irisSessionId: String = ""
)