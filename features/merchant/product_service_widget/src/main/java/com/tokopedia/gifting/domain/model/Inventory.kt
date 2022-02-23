package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Inventory {
    @SerializedName("WarehouseID")
    @Expose
    var warehouseID: String? = null

    @SerializedName("Price")
    @Expose
    var price: Double? = null

    @SerializedName("Stock")
    @Expose
    var stock: String? = null

    @SerializedName("UnlimitedStock")
    @Expose
    var unlimitedStock: Boolean? = null
}