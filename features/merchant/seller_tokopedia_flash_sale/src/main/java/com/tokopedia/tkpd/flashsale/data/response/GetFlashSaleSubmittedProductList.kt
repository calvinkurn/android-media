package com.tokopedia.tkpd.flashsale.data.response

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

    data class Warehouse(
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: Double,
        @SerializedName("rejection_reason")
        val rejectionReason: String,
        @SerializedName("status_id")
        val statusId: Long,
        @SerializedName("status_text")
        val statusText: String,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("warehouse_id")
        val warehouseId: Long
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
        @SerializedName("warehouses")
        val warehouses: List<Warehouse>
    )
}