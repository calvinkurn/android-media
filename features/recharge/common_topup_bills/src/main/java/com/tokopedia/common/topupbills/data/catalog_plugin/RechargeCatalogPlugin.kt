package com.tokopedia.common.topupbills.data.catalog_plugin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeCatalogPlugin (
    @SerializedName("add_to_mybills")
    @Expose
    val myBills: RechargeCatalogPluginMyBills = RechargeCatalogPluginMyBills(),
    @SerializedName("instant_checkout")
    @Expose
    val instantCheckout: RechargeCatalogPluginInstantCheckout = RechargeCatalogPluginInstantCheckout(),
    @SerializedName("buy_button")
    @Expose
    val buyInfo: RechargeCatalogPluginBuyInfo = RechargeCatalogPluginBuyInfo()
) {
    data class Response (
        @SerializedName("rechargeCatalogPlugin")
        @Expose
        val response: RechargeCatalogPlugin? = null
    )
}