package com.tokopedia.common.topupbills.data.catalog_plugin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeCatalogPluginInstantCheckout {
    @SerializedName("is_enabled")
    @Expose
    val isEnabled: Boolean = false
    @SerializedName("attributes")
    @Expose
    val attributes: Attributes = Attributes()

    class Attributes {
        @SerializedName("text")
        @Expose
        val text: String = ""
    }
}