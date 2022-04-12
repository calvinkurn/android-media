package com.tokopedia.shopdiscount.manage_discount.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

@SuppressLint("Invalid Data Type")
data class GetSlashPriceSetupProductListResponse(
    @SerializedName("getSlashPriceSetupProductList")
    @Expose
    var getSlashPriceSetupProductList: GetSlashPriceSetupProductList = GetSlashPriceSetupProductList()
) {
    data class GetSlashPriceSetupProductList(
        @SerializedName("response_header")
        @Expose
        var responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("product_list")
        @Expose
        var productList: List<ProductList> = listOf(),
    ) {
        data class ProductList(
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
            @SerializedName("is_expand")
            @Expose
            var isExpand: Boolean = false,
            @SerializedName("warehouses")
            @Expose
            var warehouses: List<Warehouses> = listOf(),
            @SerializedName("slash_price_info")
            @Expose
            var slashPriceInfo: SlashPriceInfo = SlashPriceInfo(),
            @SerializedName("variants")
            @Expose
            var variants: List<Variant> = listOf()
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
                @SerializedName("abusive_rule")
                @Expose
                var abusiveRule: Boolean = false,
                @SerializedName("avg_sold_price")
                @Expose
                var avgSoldPrice: Int = 0,
                @SerializedName("cheapest_price")
                @Expose
                var cheapestPrice: Int = 0,
                @SerializedName("discounted_price")
                @Expose
                var discountedPrice: Int = 0,
                @SerializedName("discounted_percentage")
                @Expose
                var discountedPercentage: Int = 0,
                @SerializedName("min_recommendation_price")
                @Expose
                var minRecommendationPrice: Int = 0,
                @SerializedName("min_recommendation_percentage")
                @Expose
                var minRecommendationPercentage: Int = 0,
                @SerializedName("max_recommendation_price")
                @Expose
                var maxRecommendationPrice: Int = 0,
                @SerializedName("max_recommendation_percentage")
                @Expose
                var maxRecommendationPercentage: Int = 0,
                @SerializedName("disable")
                @Expose
                var disable: Boolean = false,
                @SerializedName("disable_recommendation")
                @Expose
                var disableRecommendation: Boolean = false,
                @SerializedName("warehouse_type")
                @Expose
                var warehouseType: Int = 0,
                @SerializedName("original_price")
                @Expose
                var originalPrice: Int = 0
            )

            data class SlashPriceInfo(
                @SerializedName("slash_price_product_id")
                @Expose
                var slashPriceProductId: String = "",
                @SerializedName("discounted_price")
                @Expose
                var discountedPrice: Int = 0,
                @SerializedName("discount_percentage")
                @Expose
                var discountPercentage: Int = 0,
                @SerializedName("start_date")
                @Expose
                var startDate: String = "",
                @SerializedName("end_date")
                @Expose
                var endDate: String = "",
                @SerializedName("slash_price_status_id")
                @Expose
                var slashPriceStatusId: String = ""
            )

            data class Variant(
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
                @SerializedName("is_expand")
                @Expose
                var isExpand: Boolean = false,
                @SerializedName("warehouses")
                @Expose
                var warehouses: ArrayList<Warehouses> = arrayListOf(),
                @SerializedName("slash_price_info")
                @Expose
                var slashPriceInfo: SlashPriceInfo = SlashPriceInfo()
            )

        }
    }
}