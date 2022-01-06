package com.tokopedia.tokopedianow.common.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel

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
        @SerializedName("shop_id")
        @SuppressLint("Invalid Data Type")
        val shopId: Int,
        @Expose
        @SerializedName("warehouse_id")
        @SuppressLint("Invalid Data Type")
        val warehouseId: Int,
        @Expose
        @SerializedName("service_type")
        val serviceType: String,
        @Expose
        @SerializedName("warehouses")
        val warehouses: List<LocalWarehouseModel>
    )
}