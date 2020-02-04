package com.tokopedia.vouchergame.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.product.CatalogProductInput

/**
 * Created by resakemal on 26/11/19.
 */
open class VoucherGameDetailData(

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
        val product: VoucherGameProductData = VoucherGameProductData()

) {
        class Response(
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                val response: VoucherGameDetailData = VoucherGameDetailData()
        )
}