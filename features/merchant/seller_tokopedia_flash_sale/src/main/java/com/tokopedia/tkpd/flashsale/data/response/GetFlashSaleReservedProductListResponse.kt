package com.tokopedia.tkpd.flashsale.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetFlashSaleReservedProductListResponse(
    @SerializedName("getFlashSaleReservedProductList")
    val getFlashSaleReservedProductList: GetFlashSaleReservedProductList = GetFlashSaleReservedProductList()
) {
    data class GetFlashSaleReservedProductList(
        @SerializedName("product_list")
        val productList: List<Product> = listOf(),
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("total_product")
        val totalProduct: Int = 0
    ) {
        data class Product(
            @SerializedName("child_products")
            val childProducts: List<ChildProduct> = listOf(),
            @SerializedName("is_multiwarehouse")
            val isMultiWarehouse: Boolean = false,
            @SerializedName("is_parent_product")
            val isParentProduct: Boolean = false,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("picture")
            val picture: String = "",
            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            val price: Price = Price(),
            @SerializedName("product_criteria")
            val productCriteria: ProductCriteria = ProductCriteria(),
            @SerializedName("product_id")
            val productId: Long = 0,
            @SerializedName("sku")
            val sku: String = "",
            @SerializedName("stock")
            val stock: Int = 0,
            @SerializedName("url")
            val url: String = "",
            @SerializedName("warehouses")
            val warehouses: List<Warehouse> = listOf()
        ) {

            data class Warehouse(
                @SerializedName("warehouse_id")
                val warehouseId: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("stock")
                val stock: Long = 0,
                @SerializedName("price")
                val price: String = "",
                @SerializedName("discount_setup")
                val discountSetup: DiscountSetup,
                @SerializedName("is_dilayani_tokopedia")
                val isDilayaniTokopedia: Boolean = false,
                @SerializedName("is_toggle_on")
                val isToggleOn: Boolean = false,
                @SerializedName("is_disabled")
                val isDisabled: Boolean = false,
                @SerializedName("disabled_reason")
                val disabledReason: String = ""
            ) {
                data class DiscountSetup(
                    @SerializedName("discount")
                    val discount: Int = 0,
                    @SerializedName("price")
                    val price: String = "",
                    @SerializedName("stock")
                    val stock: Long = 0
                )
            }

            data class ChildProduct(
                @SerializedName("disabled_reason")
                val disabledReason: String = "",
                @SerializedName("is_disabled")
                val isDisabled: Boolean = false,
                @SerializedName("is_multiwarehouse")
                val isMultiwarehouse: Boolean = false,
                @SerializedName("is_toggle_on")
                val isToggleOn: Boolean = false,
                @SerializedName("name")
                val name: String = "",
                @SerializedName("picture")
                val picture: String = "",
                @SuppressLint("Invalid Data Type")
                @SerializedName("price")
                val price: Price = Price(),
                @SerializedName("product_criteria")
                val productCriteria: ProductCriteria = ProductCriteria(),
                @SerializedName("product_id")
                val productId: Long = 0,
                @SerializedName("sku")
                val sku: String = "",
                @SerializedName("stock")
                val stock: Int = 0,
                @SerializedName("url")
                val url: String = "",
                @SerializedName("warehouses")
                val warehouses: List<Warehouse> = listOf()
            ) {
                data class Price(
                    @SerializedName("lower_price")
                    val lowerPrice: String = "",
                    @SerializedName("price")
                    val price: String = "",
                    @SerializedName("upper_price")
                    val upperPrice: String = ""
                )

                data class Warehouse(
                    @SerializedName("disabled_reason")
                    val disabledReason: String = "",
                    @SerializedName("discount_setup")
                    val discountSetup: DiscountSetup = DiscountSetup(),
                    @SerializedName("is_dilayani_tokopedia")
                    val isDilayaniTokopedia: Boolean = false,
                    @SerializedName("is_disabled")
                    val isDisabled: Boolean = false,
                    @SerializedName("is_toggle_on")
                    val isToggleOn: Boolean = false,
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("price")
                    val price: String = "",
                    @SerializedName("stock")
                    val stock: Long = 0,
                    @SerializedName("warehouse_id")
                    val warehouseId: String = ""
                ) {
                    data class DiscountSetup(
                        @SerializedName("discount")
                        val discount: Int = 0,
                        @SerializedName("price")
                        val price: String = "",
                        @SerializedName("stock")
                        val stock: Long = 0
                    )
                }
            }

            data class Price(
                @SerializedName("lower_price")
                val lowerPrice: String = "",
                @SerializedName("price")
                val price: String = "",
                @SerializedName("upper_price")
                val upperPrice: String = "",
            )

            data class ProductCriteria(
                @SerializedName("criteria_id")
                val criteriaId: String = "",
                @SerializedName("max_custom_stock")
                val maxCustomStock: Int = 0,
                @SerializedName("max_discount")
                val maxDiscount: Long = 0,
                @SerializedName("max_final_price")
                val maxFinalPrice: String = "",
                @SerializedName("min_custom_stock")
                val minCustomStock: Int = 0,
                @SerializedName("min_discount")
                val minDiscount: Long = 0,
                @SerializedName("min_final_price")
                val minFinalPrice: String = "",
            )
        }

        data class ResponseHeader(
            @SerializedName("error_code")
            val errorCode: Int = 0,
            @SerializedName("error_message")
            val errorMessage: List<String> = listOf(),
            @SerializedName("process_time")
            val processTime: Int = 0,
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}
