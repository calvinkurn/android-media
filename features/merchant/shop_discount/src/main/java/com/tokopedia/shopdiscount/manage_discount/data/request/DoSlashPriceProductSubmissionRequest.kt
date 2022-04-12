package com.tokopedia.shopdiscount.manage_discount.data.request

import androidx.annotation.StringDef
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest.DoSlashPriceSubmissionAction.Companion.CREATE
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest.DoSlashPriceSubmissionAction.Companion.UPDATE

data class DoSlashPriceProductSubmissionRequest(
    @SerializedName("request_header")
    @Expose
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("action")
    @Expose
    var action: String = "",
    @SerializedName("slash_price_products")
    @Expose
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
        @Expose
        var productId: String = "",
        @SerializedName("start_time_unix")
        @Expose
        var startTimeUnix: String = "",
        @SerializedName("end_time_unix")
        @Expose
        var endTimeUnix: String = "",
        @SerializedName("slash_price_warehouses")
        @Expose
        var slashPriceWarehouses: List<SlashPriceWarehouse> = listOf()
    ) {
        data class SlashPriceWarehouse(
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
                @SerializedName("max_order")
                @Expose
                var maxOrder: String = "",
                @SerializedName("discounted_price")
                @Expose
                var discountedPrice: Int = 0,
                @SerializedName("discounted_percentage")
                @Expose
                var discountedPercentage: Int = 0,
                @SerializedName("enable")
                @Expose
                var enable: Boolean = false
            )
        }
    }
}