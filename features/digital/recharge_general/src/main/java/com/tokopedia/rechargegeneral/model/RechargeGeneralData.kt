package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
class RechargeGeneralData(

        @SerializedName("needEnquiry")
        @Expose
        val needEnquiry: Boolean = true,
        @SerializedName("isShowingProduct")
        @Expose
        val isShowingProduct: Boolean = true,
        @SerializedName("enquiryFields")
        @Expose
        val enquiryFields: List<RechargeGeneralInput> = listOf(),
        @SerializedName("product")
        @Expose
        val product: RechargeGeneralItemData = RechargeGeneralItemData()

) {
        class Response(
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                val response: RechargeGeneralData = RechargeGeneralData()
        )
}