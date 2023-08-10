package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.gifting.domain.model.Inventory
import com.tokopedia.gifting.domain.model.Shop


data class Addon (
    @SerializedName("Basic")
    var basic: Basic = Basic(),

    @SerializedName("Inventory")
    var inventory: Inventory = Inventory(),

    @SerializedName("Warehouse")
    var warehouse: Warehouse = Warehouse(),

    @SerializedName("Shop")
    var shop: Shop = Shop()
)
