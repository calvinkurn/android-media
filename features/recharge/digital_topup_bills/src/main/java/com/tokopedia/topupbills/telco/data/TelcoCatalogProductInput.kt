package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
data class TelcoCatalogProductInput(
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("needEnquiry")
        @Expose
        val needEnquiry: Boolean = false,
        @SerializedName("isShowingProduct")
        @Expose
        val isShowingProduct: Boolean = false,
        @SerializedName("enquiryFields")
        @Expose
        val enquiryFields: List<TelcoCatalogEnquiryFields> = listOf(),
        @SerializedName("product")
        @Expose
        val product: TelcoCatalogProduct = TelcoCatalogProduct(),
        @SerializedName("filterTagComponents")
        @Expose
        val filterTagComponents: List<TelcoFilterTagComponent> = mutableListOf())

data class TelcoCatalogEnquiryFields(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("param_name")
        @Expose
        val paramName: String = "",
        @SerializedName("name")
        @Expose
        val name: String = ""
)
