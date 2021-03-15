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
        var error: Error = Error(),
        @SerializedName("data")
        var data: DataWhitelist = DataWhitelist()
)

data class DataWhitelist(
        @SerializedName("eligibility_state")
        var eligibilityState: Int = -1
)
