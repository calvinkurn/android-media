package com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.response

import com.google.gson.annotations.SerializedName

data class UpdateDynamicDataResponse(
    @SerializedName("update_dynamic_data")
    val updateDynamicData: UpdateDynamicData = UpdateDynamicData()
) {
    data class UpdateDynamicData(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessages: List<String> = emptyList(),
        @SerializedName("data")
        val data: Data = Data()
    ) {
        data class Data(
            @SerializedName("dynamic_data")
            val dynamicData: String = ""
        )
    }
}
