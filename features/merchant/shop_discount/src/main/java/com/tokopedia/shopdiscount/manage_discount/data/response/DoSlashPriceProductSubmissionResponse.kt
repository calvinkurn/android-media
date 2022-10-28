package com.tokopedia.shopdiscount.manage_discount.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class DoSlashPriceProductSubmissionResponse(
    @SerializedName("DoSlashPriceProductSubmission")
    @Expose
    var doSlashPriceProductSubmission: DoSlashPriceProductSubmission = DoSlashPriceProductSubmission()
) {
    data class DoSlashPriceProductSubmission(
        @SerializedName("response_header")
        @Expose
        var responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("data")
        @Expose
        var data: List<Data> = listOf()
    ) {
        data class Data(
            @SerializedName("product_id")
            @Expose
            var productId: String = "",
            @SerializedName("name")
            @Expose
            var name: String = "",
            @SerializedName("sku")
            @Expose
            var sku: String = "",
            @SerializedName("picture")
            @Expose
            var picture: String = "",
            @SerializedName("success")
            @Expose
            var success: Boolean = false,
            @SerializedName("message")
            @Expose
            var message: String = "",
            @SerializedName("error_code")
            @Expose
            var errorCode: Int = 0,
            @SerializedName("warehouses")
            @Expose
            var warehouses: ArrayList<Warehouses> = arrayListOf()

        ) {
            data class Warehouses(
                @SerializedName("key")
                @Expose
                var key: String = "",
                @SerializedName("value")
                @Expose
                var value: Value = Value()

            ) {
                data class Value(
                    @SerializedName("warehouse_id")
                    @Expose
                    var warehouseId: String = "",
                    @SerializedName("success")
                    @Expose
                    var success: Boolean = false,
                    @SerializedName("message")
                    @Expose
                    var message: String = "",
                    @SerializedName("error_code")
                    @Expose
                    var errorCode: Int = 0
                )
            }
        }
    }
}