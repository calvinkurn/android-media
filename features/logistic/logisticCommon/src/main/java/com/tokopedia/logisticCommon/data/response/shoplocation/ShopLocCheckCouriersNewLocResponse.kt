package com.tokopedia.logisticCommon.data.response.shoplocation

import com.google.gson.annotations.SerializedName

data class ShopLocCheckCouriersNewLocResponse (
        @SerializedName("ShopLocCheckCouriersNewLoc")
        var shopLocCheckCouriers: ShopLocCheckCouriers = ShopLocCheckCouriers()
)

data class ShopLocCheckCouriers(
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("message")
        var message: String = "",
        @SerializedName("error")
        var error: Error = Error(),
        @SerializedName("data")
        var data: DataCheckCouriers = DataCheckCouriers()
)

data class DataCheckCouriers(
        @SerializedName("is_covered")
        var isCovered: Boolean = true
)