package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
class RechargeGeneralProductData(

        @SerializedName("needEnquiry")
        @Expose
        val needEnquiry: Boolean = true,
        @SerializedName("isShowingProduct")
        @Expose
        val isShowingProduct: Boolean = true,
        @SerializedName("enquiryFields")
        @Expose
        val enquiryFields: List<RechargeGeneralProductInput> = listOf(),
        @SerializedName("product")
        @Expose
        val product: RechargeGeneralProductItemData = RechargeGeneralProductItemData()

) {
        class Response(
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                val response: RechargeGeneralProductData = RechargeGeneralProductData()
        )
}