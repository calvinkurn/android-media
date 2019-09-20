package com.tokopedia.vouchergame.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameDetailData(

        @SerializedName("needEnquiry")
        @Expose
        val needEnquiry: Boolean = true,
        @SerializedName("isShowingProduct")
        @Expose
        val isShowingProduct: Boolean = true,
        @SerializedName("enquiryFields")
        @Expose
        val enquiryFields: List<VoucherGameEnquiryFields> = listOf(),
        @SerializedName("product")
        @Expose
        var product: VoucherGameProductData = VoucherGameProductData()

) {
        class Response(
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                val response: VoucherGameDetailData = VoucherGameDetailData()
        )
}