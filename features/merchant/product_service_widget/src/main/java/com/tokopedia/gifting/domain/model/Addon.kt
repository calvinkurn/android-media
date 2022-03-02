package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Addon {
    @SerializedName("Basic")
    @Expose
    var basic: Basic = Basic()

    @SerializedName("Inventory")
    @Expose
    var inventory: Inventory = Inventory()

    @SerializedName("Warehouse")
    @Expose
    var warehouse: Warehouse = Warehouse()

    @SerializedName("Shop")
    @Expose
    var shop: Shop = Shop()
}