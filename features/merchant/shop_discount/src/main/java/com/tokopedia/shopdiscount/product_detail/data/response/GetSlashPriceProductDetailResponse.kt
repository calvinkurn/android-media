package com.tokopedia.shopdiscount.product_detail.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@SuppressLint("Invalid Data Type")
data class GetSlashPriceProductDetailResponse(
    @SerializedName("GetSlashPriceProductDetail")
    @Expose
    var getSlashPriceProductDetail: GetSlashPriceProductDetail = GetSlashPriceProductDetail()
) {
    data class GetSlashPriceProductDetail(
        @SerializedName("response_header")
        @Expose
        var responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("product_list")
        @Expose
        var productList: List<ProductList> = listOf()
    ) {
        data class ResponseHeader(
            @SerializedName("status")
            @Expose
            var status: String = ""
        )

        data class ProductList(
            @SerializedName("slash_price_product_id")
            @Expose
            var slashPriceProductId: String = "",
            @SerializedName("product_id")
            @Expose
            var productId: String = "",
            @SerializedName("name")
            @Expose
            var name: String = "",
            @SerializedName("price")
            @Expose
            var price: Price = Price(),
            @SerializedName("stock")
            @Expose
            var stock: String = "",
            @SerializedName("url")
            @Expose
            var url: String = "",
            @SerializedName("sku")
            @Expose
            var sku: String = "",
            @SerializedName("picture")
            @Expose
            var picture: String = "",
            @SerializedName("discounted_price")
            @Expose
            var discountedPrice: Int = 0,
            @SerializedName("discounted_percentage")
            @Expose
            var discountedPercentage: Int = 0,
            @SerializedName("max_order")
            @Expose
            var maxOrder: String = "",
            @SerializedName("start_date")
            @Expose
            var startDate: String = "",
            @SerializedName("end_date")
            @Expose
            var endDate: String = "",
            @SerializedName("warehouses")
            @Expose
            var warehouses: List<Warehouses> = listOf(),
            @SerializedName("is_variant")
            @Expose
            var isVariant: Boolean = false,
            @SerializedName("is_expand")
            @Expose var isExpand: Boolean = false,
            @SerializedName("parent_id")
            @Expose
            var parentId: String = ""
        ) {
            data class Price(
                @SerializedName("min")
                @Expose
                var min: Int = 0,
                @SerializedName("min_formatted")
                @Expose
                var minFormatted: String = "",
                @SerializedName("max")
                @Expose
                var max: Int = 0,
                @SerializedName("max_formatted")
                @Expose
                var maxFormatted: String = ""
            )

            data class Warehouses(
                @SerializedName("warehouse_id")
                @Expose
                var warehouseId: String = "",
                @SerializedName("warehouse_name")
                @Expose
                var warehouseName: String = "",
                @SerializedName("warehouse_location")
                @Expose
                var warehouseLocation: String = "",
                @SerializedName("warehouse_stock")
                @Expose
                var warehouseStock: String = "",
                @SerializedName("max_order")
                @Expose
                var maxOrder: String = "",
                @SerializedName("event_id")
                @Expose
                var eventId: String = "",
                @SerializedName("discounted_price")
                @Expose
                var discountedPrice: Int = 0,
                @SerializedName("discounted_percentage")
                @Expose
                var discountedPercentage: Int = 0,
                @SerializedName("original_price")
                @Expose
                var originalPrice: Int = 0
            )
        }
    }

}