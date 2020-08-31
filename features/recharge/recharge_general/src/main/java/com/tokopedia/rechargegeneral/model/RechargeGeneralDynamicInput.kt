package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeGeneralDynamicInput(
        @SerializedName("needEnquiry")
        @Expose
        val needEnquiry: Boolean = true,
        @SerializedName("isShowingProduct")
        @Expose
        val isShowingProduct: Boolean = true,
        @SerializedName("dynamicFields")
        @Expose
        val enquiryFields: List<RechargeGeneralDynamicField> = listOf()) {

    class Response(
            @SerializedName("rechargeCatalogDynamicInput")
            @Expose
            val response: RechargeGeneralDynamicInput = RechargeGeneralDynamicInput()
    )
}