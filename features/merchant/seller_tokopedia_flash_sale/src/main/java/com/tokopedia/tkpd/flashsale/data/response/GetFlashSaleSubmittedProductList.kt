package com.tokopedia.tkpd.flashsale.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetFlashSaleSubmittedProductListResponse(
    @SerializedName("getFlashSaleSubmittedProductList")
    val getFlashSaleSubmittedProductList: GetFlashSaleSubmittedProductList
) {
    data class GetFlashSaleSubmittedProductList(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader,
        @SerializedName("data")
        val data: Data
    )

    data class Data(
        @SerializedName("product_list")
        val productList: List<Product>,
        @SerializedName("total_product")
        val totalProduct: Int
    )

    data class ResponseHeader(
        @SerializedName("error_code")
        val errorCode: Int,
        @SerializedName("process_time")
        val processTime: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("success")
        val success: Boolean
    )

    data class Price(
        @SerializedName("price")
        val price: Double,
        @SerializedName("lower_price")
        val lowerPrice: Double,
        @SerializedName("upper_price")
        val upperPrice: Double
    )

    data class Discount(
        @SerializedName("discount")
        val discount: Long,
        @SerializedName("lower_discount")
        val lowerDiscount: Long,
        @SerializedName("upper_discount")
        val upperDiscount: Long
    )

    data class DiscountedPrice(
        @SerializedName("price")
        val price: Double,
        @SerializedName("lower_price")
        val lowerPrice: Double,
        @SerializedName("upper_price")
        val upperPrice: Double
    )

    data class Warehouse(
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: Double,
        @SerializedName("discount_setup")
        val discountSetup: DiscountSetup?,
        @SerializedName("subsidy")
        val subsidy: Subsidy?,
        @SerializedName("rejection_reason")
        val rejectionReason: String,
        @SerializedName("status_id")
        val statusId: Long,
        @SerializedName("status_text")
        val statusText: String,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("warehouse_id")
        val warehouseId: Long,
        @SerializedName("sold_count")
        val soldCount: Int
    )

    data class DiscountSetup(
        @SerializedName("price")
        val price: Double,
        @SerializedName("stock")
        val stock: Long,
        @SerializedName("discount")
        val discount: Long
    )

    data class Subsidy(
        @SerializedName("has_subsidy")
        val hasSubsidy: Boolean = false,
        @SerializedName("subsidy_amount")
        val subsidyAmount: Long
    )

    data class ProductCriteria(
        @SerializedName("criteria_id")
        val criteriaId: Long
    )

    data class Product(
        @SerializedName("campaign_stock")
        val campaignStock: Int,
        @SerializedName("is_multiwarehouse")
        val isMultiwarehouse: Boolean,
        @SerializedName("is_parent_product")
        val isParentProduct: Boolean,
        @SerializedName("total_child")
        val totalChild: Int,
        @SerializedName("sold_count")
        val soldCount: Int,
        @SerializedName("main_stock")
        val mainStock: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("picture")
        val picture: String,
        @SerializedName("product_criteria")
        val productCriteria: ProductCriteria,
        @SerializedName("product_id")
        val productId: Long,
        @SerializedName("url")
        val url: String,
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Price,
        @SerializedName("discount")
        val discount: Discount,
        @SuppressLint("Invalid Data Type")
        @SerializedName("discounted_price")
        val discountedPrice: DiscountedPrice,
        @SerializedName("warehouses")
        val warehouses: List<Warehouse>,
        @SerializedName("total_subsidy")
        val totalSubsidy: Long,
        @SerializedName("status_text")
        val statusText: String,
        @SerializedName("count_location")
        val countLocation: Int
    )
}
