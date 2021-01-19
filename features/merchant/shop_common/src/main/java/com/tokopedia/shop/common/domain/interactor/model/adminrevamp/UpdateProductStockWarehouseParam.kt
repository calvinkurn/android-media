package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
                       productId: String,
                       warehouseId: String,
                       stock: String): UpdateProductStockWarehouseParam {
            return UpdateProductStockWarehouseParam(
                    shopId = shopId,
                    productWarehouseParam = listOf(
                            ProductWarehouseParam(
                                    productId = productId,
                                    warehouseId = warehouseId,
                                    stock = stock
                            )
                    )
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