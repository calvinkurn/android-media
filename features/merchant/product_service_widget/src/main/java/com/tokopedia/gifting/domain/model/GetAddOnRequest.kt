package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.addon.domain.model.Additional

data class GetAddOnRequest(
    @SerializedName("RequestData")
    @Expose
    val requestData: RequestData = RequestData(),
    @SerializedName("GetAddOnRequest")
    @Expose
    val addOnRequest: List<AddOnRequest> = emptyList(),
    @SerializedName("Source")
    @Expose
    val source: Source = Source()
)

data class RequestData(
    @SerializedName("Inventory")
    @Expose
    val inventory: Boolean = true,
    @SerializedName("ShopInfo")
    @Expose
    val shopInfo: Boolean = true,
    @SerializedName("WarehouseInfo")
    @Expose
    val warehouseInfo: Boolean = true,
    @SerializedName("StaticInfo")
    @Expose
    val staticInfo: Boolean = true,
    @SerializedName("AggregatedData")
    @Expose
    val aggregatedData: Boolean = true
)

data class AddOnRequest(
    @SerializedName("AddOnID")
    @Expose
    val addOnID: String = "",
    @SerializedName("AddOnType")
    @Expose
    val addOnType: String = "",
    @SerializedName("Additional")
    @Expose
    val additional: Additional = Additional(),
)

data class Source(
    @SerializedName("Usecase")
    @Expose
    val usecase: String = "",
    @SerializedName("Squad")
    @Expose
    val squad: String = ""
)
