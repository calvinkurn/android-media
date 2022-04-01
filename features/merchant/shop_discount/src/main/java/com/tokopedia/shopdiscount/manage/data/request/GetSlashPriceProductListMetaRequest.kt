package com.tokopedia.shopdiscount.manage.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetSlashPriceProductListMetaRequest(
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