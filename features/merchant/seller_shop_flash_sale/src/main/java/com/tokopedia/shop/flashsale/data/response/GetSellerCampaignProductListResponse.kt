package com.tokopedia.shop.flashsale.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetSellerCampaignProductListResponse(
    @SerializedName("getSellerCampaignProductList")
    val getSellerCampaignProductList: GetSellerCampaignProductList = GetSellerCampaignProductList()
) {
    data class GetSellerCampaignProductList(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("product_list")
        val productList: List<ProductList> = listOf(),
        @SerializedName("total_product")
        val totalProduct: Int = 0,
        @SerializedName("total_product_sold")
        val totalProductSold: Int = 0,
        @SerializedName("count_accepted_product")
        val countAcceptedProduct: Int = 0,
        @SerializedName("total_product_qty")
        val totalProductQty: Int = 0,
        @SerializedName("total_income")
        val totalIncome: Int = 0,
        @SerializedName("total_income_formatted")
        val totalIncomeFormatted: String = "",
        @SerializedName("product_failed_count")
        val productFailedCount: Int = 0
    )

    data class ResponseHeader(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("errorMessage")
        val errorMessage: List<String> = listOf(),
        @SerializedName("success")
        val success: Boolean = false,
        @SerializedName("processTime")
        val processTime: Float = 0F
    )

    data class ProductList(
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("parent_id")
        val parentId: String = "",
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("product_url")
        val productUrl: String = "",
        @SerializedName("product_sku")
        val productSku: String = "",
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("formatted_price")
        val formattedPrice: String = "",
        @SerializedName("image_url")
        val imageUrl: ImageUrl = ImageUrl(),
        @SerializedName("product_map_data")
        val productMapData: ProductMapData = ProductMapData(),
        @SerializedName("warehouse_list")
        val warehouseList: List<WarehouseData> = listOf(),
        @SerializedName("view_count")
        val viewCount: Int = 0,
        @SerializedName("highlight_product_wording")
        val highlightProductWording: String = "",
    )

    data class ImageUrl(
        @SerializedName("img_100square")
        val img100Square: String = "",
        @SerializedName("img_200")
        val img200: String = "",
        @SerializedName("img_300")
        val img300: String = "",
        @SerializedName("img_700")
        val img700: String = "",
    )

    data class ProductMapData(
        @SerializedName("product_map_id")
        val productMapId: String = "",
        @SerializedName("campaign_id")
        val campaignId: String = "",
        @SerializedName("product_map_status")
        val productMapStatus: Int = 0,
        @SerializedName("product_map_admin_status")
        val productMapAdminStatus: Int = 0,
        @SuppressLint("Invalid Data Type") // Server still using integer number data type
        @SerializedName("original_price")
        val originalPrice: Long = 0,
        @SuppressLint("Invalid Data Type") // Server still using integer number data type
        @SerializedName("discounted_price")
        val discountedPrice: Long = 0,
        @SerializedName("discount_percentage")
        val discountPercentage: Int = 0,
        @SerializedName("custom_stock")
        val customStock: Long = 0,
        @SerializedName("original_custom_stock")
        val originalCustomStock: Int = 0,
        @SerializedName("original_stock")
        val originalStock: Int = 0,
        @SerializedName("campaign_sold_count")
        val campaignSoldCount: Int = 0,
        @SerializedName("max_order")
        val maxOrder: Int = 0
    )

    data class WarehouseData(
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @SerializedName("warehouse_name")
        val warehouseName: String = "",
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("chosen_warehouse")
        val chosenWarehouse: Boolean = false,
        @SerializedName("original_custom_stock")
        val originalCustomStock: Int = 0,
        @SerializedName("custom_stock")
        val customStock: Int = 0,
    )
}
