package com.tokopedia.tradein.model.request


import com.google.gson.annotations.SerializedName

data class Laku6TestDataModel(
    @SerializedName("device_info")
    var deviceInfo: DeviceInfo,
    @SerializedName("root_blocked")
    var rootBlocked: Boolean
) {
    data class DeviceInfo(
        @SerializedName("brand")
        var brand: String,
        @SerializedName("model_display_name")
        var modelDisplayName: String,
        @SerializedName("model_id")
        var modelId: String,
        @SerializedName("model_name")
        var modelName: String,
        @SerializedName("ram")
        var ram: String,
        @SerializedName("storage")
        var storage: String
    )
}