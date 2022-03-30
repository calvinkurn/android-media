package com.tokopedia.shopdiscount.bulk.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetSlashPriceBenefitRequest(
    @SerializedName("source")
    @Expose
    var source: String = "",
    @SerializedName("ip")
    @Expose
    var ip: String = "",
    @SerializedName("usecase")
    @Expose
    var usecase: String = ""
)