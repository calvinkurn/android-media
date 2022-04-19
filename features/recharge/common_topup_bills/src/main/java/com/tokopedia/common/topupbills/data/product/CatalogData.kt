package com.tokopedia.common.topupbills.data.product

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
        val product: CatalogProductData = CatalogProductData()

) {
        class Response(
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                val response: CatalogData = CatalogData()
        )
}