package com.tokopedia.shopdiscount.subsidy.model.request

import com.google.gson.annotations.SerializedName

data class SubsidyEngineGetSellerOutReasonListRequest(
    @SerializedName("RequestHeader")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("UserId")
    var userId: String = ""
){
    data class RequestHeader(
        @SerializedName("Source")
        var source: String = "android",
        @SerializedName("IP")
        var ip: String = "",
        @SerializedName("Usecase")
        var usecase: String = "",
        @SerializedName("Version")
        var version: String = "android 2.0.0"
    ){
        companion object {
            const val USE_CASE_SLASH_PRICE = "SP"
        }
    }
}
