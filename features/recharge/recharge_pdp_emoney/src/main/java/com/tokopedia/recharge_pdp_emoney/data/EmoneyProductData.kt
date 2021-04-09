package com.tokopedia.recharge_pdp_emoney.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
data class EmoneyProductData(

        @SerializedName("needEnquiry")
        @Expose
        var needEnquiry: Boolean = true,
        @SerializedName("isShowingProduct")
        @Expose
        var isShowingProduct: Boolean = true,
        @SerializedName("enquiryFields")
        @Expose
        var enquiryFields: List<EnquiryProductInput> = listOf()

) {
    class Response(
            @SerializedName("rechargeCatalogProductInput")
            @Expose
            var response: EmoneyProductData = EmoneyProductData()
    )
}