package com.tokopedia.shopdiscount.manage.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

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
        data class ResponseHeader(
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )

        data class SlashPriceProduct(
            @SerializedName("discount_percentage_data")
            val discountPercentageData: DiscountPercentageData = DiscountPercentageData(),
            @SerializedName("discounted_percentage")
            val discountedPercentage: Int = 0,
            @SerializedName("discounted_price")
            val discountedPrice: Int = 0,
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
            val warehouses: List<Any> = listOf()
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
        }
    }
}