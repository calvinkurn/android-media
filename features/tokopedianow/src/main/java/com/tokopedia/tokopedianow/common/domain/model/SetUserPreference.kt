package com.tokopedia.tokopedianow.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

data class SetUserPreference(
    @Expose
    @SerializedName("TokonowSetUserPreference")
    val response: SetUserPreferenceResponse
) {

    data class SetUserPreferenceResponse(
        @Expose
        @SerializedName("header")
        val header: Header,
        @Expose
        @SerializedName("data")
        val data: SetUserPreferenceData
    )

    data class SetUserPreferenceData(
        @Expose
        @SerializedName("shopID")
        val shopId: String,
        @Expose
        @SerializedName("warehouseID")
        val warehouseId: String,
        @Expose
        @SerializedName("serviceType")
        val serviceType: String,
        @Expose
        @SerializedName("warehouses")
        val warehouses: List<WarehouseData>
    )
}