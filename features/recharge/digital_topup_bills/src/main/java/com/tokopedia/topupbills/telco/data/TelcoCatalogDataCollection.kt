package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TelcoCatalogDataCollection(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("products")
        @Expose
        val products: List<TelcoProduct> = listOf())