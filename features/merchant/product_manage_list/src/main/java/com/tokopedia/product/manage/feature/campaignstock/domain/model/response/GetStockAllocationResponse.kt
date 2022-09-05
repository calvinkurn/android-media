package com.tokopedia.product.manage.feature.campaignstock.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.product.manage.common.feature.variant.data.model.CampaignType

data class GetStockAllocationResponse(
        @Expose
        @SerializedName("GetStockAllocation")
        val getStockAllocation: GetStockAllocation = GetStockAllocation()
)

data class GetStockAllocation(
        @Expose
        @SerializedName("data")
        val data: List<GetStockAllocationData> = listOf(),
        @Expose
        @SerializedName("header")
        val header: Header = Header()
)

data class GetStockAllocationData(
        @Expose
        @SerializedName("summary")
        val summary: GetStockAllocationSummary = GetStockAllocationSummary(),
        @Expose
        @SerializedName("detail")
        val detail: GetStockAllocationDetail = GetStockAllocationDetail()
)

data class GetStockAllocationSummary(
        @Expose
        @SerializedName("is_variant")
        val isVariant: Boolean = false,
        @Expose
        @SerializedName("product_name")
        val productName: String = "",
        @Expose
        @SerializedName("sellable_stock")
        val sellableStock: String = "",
        @Expose
        @SerializedName("reserve_stock")
        val reserveStock: String = "",
        @Expose
        @SerializedName("total_stock")
        val totalStock: String = ""
)

data class GetStockAllocationDetail(
        @Expose
        @SerializedName("sellable")
        val sellable: List<GetStockAllocationDetailSellable> = listOf(),
        @Expose
        @SerializedName("reserve")
        val reserve: List<GetStockAllocationDetailReserve> = listOf()
)

data class GetStockAllocationDetailSellable(
        @Expose
        @SerializedName("product_id")
        val productId: String = "",
        @Expose
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @Expose
        @SerializedName("product_name")
        val productName: String = "",
        @Expose
        @SerializedName("stock")
        val stock: String = "",
        @Expose
        @SerializedName("campaign_types")
        val campaignTypeList: List<CampaignType>? = listOf()
)

data class GetStockAllocationDetailReserve(
        @Expose
        @SerializedName("event_info")
        val eventInfo: GetStockAllocationEventInfo = GetStockAllocationEventInfo()
)

data class GetStockAllocationEventInfo(
        @Expose
        @SerializedName("event_type")
        val eventType: String = "",
        @Expose
        @SerializedName("event_name")
        val eventName: String = "",
        @Expose
        @SerializedName("description")
        val description: String = "",
        @Expose
        @SerializedName("stock")
        val stock: String = "",
        @SerializedName("campaign_type")
        @Expose
        val campaignType: GetStockAllocationReservedCampaignType = GetStockAllocationReservedCampaignType(),
        @SerializedName("start_time")
        @Expose
        val startTimeNanos: String = "",
        @SerializedName("end_time")
        @Expose
        val endTimeNanos: String = "",
        @Expose
        @SerializedName("product")
        val product: List<GetStockAllocationReservedProduct> = listOf()
)

data class GetStockAllocationReservedCampaignType(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("icon_url")
        @Expose
        val iconUrl: String = ""
)

data class GetStockAllocationReservedProduct(
        @Expose
        @SerializedName("product_id")
        val productId: String = "",
        @Expose
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @Expose
        @SerializedName("product_name")
        val productName: String = "",
        @Expose
        @SerializedName("description")
        val description: String = "",
        @Expose
        @SerializedName("stock")
        val stock: String = ""
)