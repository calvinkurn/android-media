package com.tokopedia.common.topupbills.data.catalog_plugin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeCatalogPluginMyBills {
    @SerializedName("is_enabled")
    @Expose
    val isEnabled: Boolean = false
    @SerializedName("attributes")
    @Expose
    val attributes: Attributes = Attributes()

    class Attributes {
        @SerializedName("client_numbers")
        @Expose
        val clientNumbers: List<ClientNumber> = listOf()
    }

    class ClientNumber {
        @SerializedName("number")
        @Expose
        val number: String = ""
        @SerializedName("status")
        @Expose
        val status: Int = 0
    }
}