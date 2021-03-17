package com.tokopedia.logisticCommon.data.response.shoplocation

import com.google.gson.annotations.SerializedName

data class SetShopLocationStatusResponse(
        @SerializedName("ShopLocSetStatus")
        var shopLocationSetStatus: ShopLocationSetStatusResponse = ShopLocationSetStatusResponse()
)

data class ShopLocationSetStatusResponse(
        @SerializedName("status_message")
        val statusMessage: String = "",
        @SerializedName("is_success")
        val isSuccess: String = ""
)

