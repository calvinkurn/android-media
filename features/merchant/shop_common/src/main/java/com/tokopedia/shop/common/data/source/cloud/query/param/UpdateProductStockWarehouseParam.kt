package com.tokopedia.shop.common.data.source.cloud.query.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.model.ProductStock

data class UpdateProductStockWarehouseParam(
        @SerializedName("shopID")
        @Expose
        val shopId: String = "",
        @SerializedName("productWarehouse")
        @Expose
        val productWarehouseParam: List<ProductWarehouseParam> = listOf()) {
    
    companion object {
        @JvmStatic
        fun mapToParam(shopId: String,
                       warehouseId: String,
                       products: List<ProductStock>): UpdateProductStockWarehouseParam {
            val productWarehouseParam = products.map {
                ProductWarehouseParam(
                    productId = it.productId,
                    warehouseId = warehouseId,
                    stock = it.stock
                )
            }
            return UpdateProductStockWarehouseParam(
                    shopId = shopId,
                    productWarehouseParam = productWarehouseParam
            )
        }
    }
    
}

data class ProductWarehouseParam(
        @SerializedName("productID")
        @Expose
        val productId: String = "",
        @SerializedName("warehouseID")
        @Expose
        val warehouseId: String = "",
        @SerializedName("stock")
        @Expose
        val stock: String = ""
)