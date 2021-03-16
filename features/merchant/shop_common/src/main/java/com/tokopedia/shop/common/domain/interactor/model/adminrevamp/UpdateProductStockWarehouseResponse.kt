package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class UpdateProductStockWarehouseResponse(
        @SerializedName("IMSUpdateProductWarehouse")
        @Expose
        val result: UpdateProductStockWarehouseResult? = UpdateProductStockWarehouseResult()
)

data class UpdateProductStockWarehouseResult(
        @SerializedName("header")
        @Expose
        val header: Header? = Header(),
        @SerializedName("data")
        @Expose
        val data: List<ProductStockWarehouse>? = listOf()) {

        fun isSuccess(): Boolean = data?.isNotEmpty() == true
}

data class ProductStockWarehouse(
        @SerializedName("product_id")
        @Expose
        val productId: String? = "",
        @SerializedName("warehouse_id")
        @Expose
        val warehouseId: String? = "",
        @SerializedName("stock")
        @Expose
        val stock: String? = "",
        @SerializedName("shop_id")
        @Expose
        val shopId: String? = "",
        @SerializedName("status")
        @Expose
        val status: Int? = null
) {
        private companion object {
                const val STATUS_ACTIVE = 1
                const val STATUS_INACTIVE = 3
        }

        fun getProductStatus(): ProductStatus? {
                return when (status) {
                        STATUS_ACTIVE -> ProductStatus.ACTIVE
                        STATUS_INACTIVE -> ProductStatus.INACTIVE
                        else -> null
                }
        }
}