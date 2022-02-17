package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Addon {
    @SerializedName("Basic")
    @Expose
    var basic: Basic? = null

    @SerializedName("Inventory")
    @Expose
    var inventory: Inventory? = null

    @SerializedName("Warehouse")
    @Expose
    var warehouse: Warehouse? = null

    @SerializedName("Shop")
    @Expose
    var shop: Shop? = null
}