package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoCatalogProductInput(
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
        val products: TelcoCatalogProduct = TelcoCatalogProduct(),
        var productType: Int = TelcoProductType.PRODUCT_GRID)

class TelcoCatalogEnquiryFields(
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
