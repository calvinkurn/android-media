package com.tokopedia.digital.productV2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
class CatalogData(

        @SerializedName("needEnquiry")
        @Expose
        val needEnquiry: Boolean = true,
        @SerializedName("isShowingProduct")
        @Expose
        val isShowingProduct: Boolean = true,
        @SerializedName("enquiryFields")
        @Expose
        val enquiryFields: List<CatalogProductInput> = listOf(),
        @SerializedName("product")
        @Expose
        var product: CatalogProduct = CatalogProduct()

) {
        class Response(
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                val response: CatalogData = CatalogData()
        )
}