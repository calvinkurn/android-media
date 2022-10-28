package com.tokopedia.shopdiscount.manage_discount.data.request

import androidx.annotation.StringDef
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest.DoSlashPriceSubmissionAction.Companion.CREATE
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest.DoSlashPriceSubmissionAction.Companion.UPDATE

data class DoSlashPriceProductSubmissionRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("action")
    var action: String = "",
    @SerializedName("slash_price_products")
    var listSubmittedSlashPriceProduct: List<SubmittedSlashPriceProduct> = listOf()
) {

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(CREATE, UPDATE)
    annotation class DoSlashPriceSubmissionAction {
        companion object {
            const val CREATE = "CREATE"
            const val UPDATE = "UPDATE"
        }
    }

    data class SubmittedSlashPriceProduct(
        @SerializedName("product_id")
        var productId: String = "",
        @SerializedName("start_time_unix")
        var startTimeUnix: String = "",
        @SerializedName("end_time_unix")
        var endTimeUnix: String = "",
        @SerializedName("slash_price_warehouses")
        var slashPriceWarehouses: List<SlashPriceWarehouse> = listOf()
    ) {
        data class SlashPriceWarehouse(
            @SerializedName("key")
            var key: String = "",
            @SerializedName("value")
            var value: Value = Value()
        ) {
            data class Value(
                @SerializedName("warehouse_id")
                var warehouseId: String = "",
                @SerializedName("max_order")
                var maxOrder: String = "",
                @SerializedName("discounted_price")
                var discountedPrice: Double = 0.0,
                @SerializedName("discounted_percentage")
                var discountedPercentage: Int = 0,
                @SerializedName("enable")
                var enable: Boolean = false
            )
        }
    }
}