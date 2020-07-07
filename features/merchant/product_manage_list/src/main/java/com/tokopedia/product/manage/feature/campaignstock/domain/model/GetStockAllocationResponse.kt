package com.tokopedia.product.manage.feature.campaignstock.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class GetStockAllocationResponse(
        @SerializedName("GetStockAllocation")
        val getStockAllocation: GetStockAllocation? = GetStockAllocation()
)

data class GetStockAllocation(
        @SerializedName("summary")
        val summary: GetStockAllocationSummary? = GetStockAllocationSummary(),
        @SerializedName("detail")
        val detail: GetStockAllocationDetail? = GetStockAllocationDetail(),
        @SerializedName("header")
        val header: Header? = Header()
)

data class GetStockAllocationSummary(
        @SerializedName("is_variant")
        val isVariant: Boolean? = false,
        @SerializedName("product_name")
        val productName: String? = "",
        @SerializedName("sellable_stock")
        val sellableStock: String? = "",
        @SerializedName("reserve_stock")
        val reserveStock: String? = "",
        @SerializedName("total_stock")
        val totalStock: String? = ""
)

data class GetStockAllocationDetail(
        @SerializedName("sellable")
        val sellable: GetStockAllocationDetailSellable? = GetStockAllocationDetailSellable(),
        @SerializedName("reserve")
        val reserve: GetStockAllocationDetailReserve? = GetStockAllocationDetailReserve()
)

data class GetStockAllocationDetailSellable(
        @SerializedName("product_id")
        val productId: String? = "",
        @SerializedName("warehouse_id")
        val warehouseId: String? = "",
        @SerializedName("product_name")
        val productName: String? = "",
        @SerializedName("stock")
        val stock: String? = ""
)

data class GetStockAllocationDetailReserve(
        @SerializedName("event_info")
        val eventInfo: GetStockAllocationEventInfo? = GetStockAllocationEventInfo()
)

data class GetStockAllocationEventInfo(
        @SerializedName("event_type")
        val eventType: String? = "",
        @SerializedName("event_name")
        val eventName: String? = "",
        @SerializedName("description")
        val description: String? = "",
        @SerializedName("stock")
        val stock: String? = "",
        @SerializedName("action_wording")
        val actionWording: String? = "",
        @SerializedName("action_url")
        val actionUrl: String? = "",
        @SerializedName("product")
        val product: GetStockAllocationReservedProduct? = GetStockAllocationReservedProduct()
)

data class GetStockAllocationReservedProduct(
        @SerializedName("product_id")
        val productId: String? = "",
        @SerializedName("warehouse_id")
        val warehouseId: String? = "",
        @SerializedName("product_name")
        val productName: String? = "",
        @SerializedName("description")
        val description: String? = "",
        @SerializedName("stock")
        val stock: String? = ""
)