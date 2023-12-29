package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

class Inventory {
    @SerializedName("Price")
    var price: Double = Int.ZERO.toDouble()

    @SerializedName("Stock")
    var stock: String = ""

    @SerializedName("WarehouseID")
    var warehouseID: String  = ""

    @SerializedName("UnlimitedStock")
    var unlimitedStock: Boolean = false

    @SerializedName("DiscountedPrice")
    var discountedPrice: Double = Int.ZERO.toDouble()
}
