package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
data class RechargeGeneralProductData(

        @SerializedName("needEnquiry")
        @Expose
        var needEnquiry: Boolean = true,
        @SerializedName("isShowingProduct")
        @Expose
        var isShowingProduct: Boolean = true,
        @SerializedName("enquiryFields")
        @Expose
        var enquiryFields: List<RechargeGeneralProductInput> = listOf(),
        @SerializedName("product")
        @Expose
        var product: RechargeGeneralProductItemData? = RechargeGeneralProductItemData()

) {
        class Response(
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                var response: RechargeGeneralProductData = RechargeGeneralProductData()
        )
}