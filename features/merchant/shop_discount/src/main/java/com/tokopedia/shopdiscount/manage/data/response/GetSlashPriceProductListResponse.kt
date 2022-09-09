package com.tokopedia.shopdiscount.manage.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class GetSlashPriceProductListResponse(
    @SerializedName("GetSlashPriceProductList")
    val getSlashPriceProductList: GetSlashPriceProductList = GetSlashPriceProductList()
) {
    data class GetSlashPriceProductList(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("slash_price_product_list")
        val slashPriceProductList: List<SlashPriceProduct> = listOf(),
        @SerializedName("total_product")
        val totalProduct: Int = 0
    ) {
        data class SlashPriceProduct(
            @SerializedName("discount_percentage_data")
            val discountPercentageData: DiscountPercentageData = DiscountPercentageData(),
            @SerializedName("discounted_percentage")
            val discountedPercentage: Int = 0,
            @SerializedName("discounted_price")
            val discountedPrice: Double = 0.0,
            @SerializedName("discounted_price_data")
            val discountedPriceData: DiscountedPriceData = DiscountedPriceData(),
            @SerializedName("end_date")
            val endDate: String = "",
            @SerializedName("is_expand")
            val isExpand: Boolean = false,
            @SerializedName("is_variant")
            val isVariant: Boolean = false,
            @SerializedName("max_order")
            val maxOrder: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("picture")
            val picture: String = "",
            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            val price: Price = Price(),
            @SerializedName("product_id")
            val productId: String = "",
            @SerializedName("sku")
            val sku: String = "",
            @SerializedName("slash_price_product_id")
            val slashPriceProductId: String = "",
            @SerializedName("start_date")
            val startDate: String = "",
            @SerializedName("stock")
            val stock: String = "",
            @SerializedName("url")
            val url: String = "",
            @SerializedName("warehouses")
            val warehouses: List<Warehouses> = listOf()
        ) {
            data class DiscountPercentageData(
                @SerializedName("max")
                val max: Int = 0,
                @SerializedName("max_formatted")
                val maxFormatted: String = "",
                @SerializedName("min")
                val min: Int = 0,
                @SerializedName("min_formatted")
                val minFormatted: String = ""
            )

            data class DiscountedPriceData(
                @SerializedName("max")
                val max: Long = 0,
                @SerializedName("max_formatted")
                val maxFormatted: String = "",
                @SerializedName("min")
                val min: Long = 0,
                @SerializedName("min_formatted")
                val minFormatted: String = ""
            )

            data class Price(
                @SerializedName("max")
                val max: Long = 0,
                @SerializedName("max_formatted")
                val maxFormatted: String = "",
                @SerializedName("min")
                val min: Long = 0,
                @SerializedName("min_formatted")
                val minFormatted: String = ""
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
                var avgSoldPrice: Double = 0.0,
                @SerializedName("cheapest_price")
                @Expose
                var cheapestPrice: Double = 0.0,
                @SerializedName("discounted_price")
                @Expose
                var discountedPrice: Double = 0.0,
                @SerializedName("discounted_percentage")
                @Expose
                var discountedPercentage: Int = 0,
                @SerializedName("min_recommendation_price")
                @Expose
                var minRecommendationPrice: Double = 0.0,
                @SerializedName("min_recommendation_percentage")
                @Expose
                var minRecommendationPercentage: Int = 0,
                @SerializedName("max_recommendation_price")
                @Expose
                var maxRecommendationPrice: Double = 0.0,
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
                var originalPrice: Double = 0.0
            )
        }
    }
}