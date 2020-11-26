package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TelcoCatalogProduct(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("placeholder")
        @Expose
        val placeholder: String = "",
        @SerializedName("validation")
        @Expose
        val validation: List<TelcoCatalogProductValidation> = listOf(),
        @SerializedName("dataCollections")
        @Expose
        val dataCollections: List<TelcoCatalogDataCollection> = listOf())


data class TelcoCatalogProductValidation(
        @SerializedName("rule")
        @Expose
        val name: String = ""
)