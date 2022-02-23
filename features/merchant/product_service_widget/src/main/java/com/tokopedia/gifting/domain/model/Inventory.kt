package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

class Inventory {
    @SerializedName("WarehouseID")
    @Expose
    var warehouseID: String? = null

    @SerializedName("Price")
    @Expose
    var price: Double = Int.ZERO.toDouble()

    @SerializedName("Stock")
    @Expose
    var stock: String = ""

    @SerializedName("UnlimitedStock")
    @Expose
    var unlimitedStock: Boolean = false
}