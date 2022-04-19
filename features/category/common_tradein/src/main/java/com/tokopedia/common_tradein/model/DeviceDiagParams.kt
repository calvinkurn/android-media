package com.tokopedia.common_tradein.model

import com.google.gson.annotations.SerializedName

class DeviceDiagParams {
    @SerializedName("ProductId")
    var productId: String? = ""

    @SerializedName("DeviceId")
    var deviceId: String? = null

    @SerializedName("NewPrice")
    var newPrice = 0

    @SerializedName("TradeInType")
    var tradeInType = 0
}