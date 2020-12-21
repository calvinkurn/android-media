package com.tokopedia.logisticCommon.data.response.shoplocation

import com.google.gson.annotations.SerializedName

data class ShopLocationUpdateWarehouseResponse (
        @SerializedName("ShopLocSetStatus")
        var shopLocUpdateWarehouse: ShopLocUpdateWarehouse = ShopLocUpdateWarehouse()
)

data class ShopLocUpdateWarehouse(
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("message")
        var message: String = "",
        @SerializedName("error")
        var error: ErrorWarehouse = ErrorWarehouse(),
        @SerializedName("data")
        var data: DataWarehouse = DataWarehouse()
)

data class ErrorWarehouse(
        @SerializedName("id")
        var id: Int = -1,
        @SerializedName("description")
        var description: String = ""
)

data class DataWarehouse(
        @SerializedName("message")
        var message: String = ""
)
