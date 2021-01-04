package com.tokopedia.logisticCommon.data.response.shoplocation

import com.google.gson.annotations.SerializedName

data class ShopLocationWhitelistResponse(
        @SerializedName("ShopLocWhitelist")
        var shopLocWhitelist: ShopLocWhitelist = ShopLocWhitelist()
)

data class ShopLocWhitelist(
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("message")
        var message: String = "",
        @SerializedName("error")
        var error: ErrorWhitelist = ErrorWhitelist(),
        @SerializedName("data")
        var data: DataWhitelist = DataWhitelist()
)

data class ErrorWhitelist(
        @SerializedName("id")
        var id: Int = -1,
        @SerializedName("description")
        var description: String = ""
)

data class DataWhitelist(
        @SerializedName("eligibility_state")
        var eligibilityState: Int = -1
)
