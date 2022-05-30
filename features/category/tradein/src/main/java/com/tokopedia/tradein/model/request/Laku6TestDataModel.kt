package com.tokopedia.tradein.model.request


import com.google.gson.annotations.SerializedName

data class Laku6TestDataModel(
    @SerializedName("device_info")
    var deviceInfo: DeviceInfo,
    @SerializedName("final_page_info")
    var finalPageInfo: FinalPageInfo,
    @SerializedName("root_blocked")
    var rootBlocked: Boolean
) {
    data class DeviceInfo(
        @SerializedName("brand")
        var brand: String,
        @SerializedName("model")
        var model: String,
        @SerializedName("model_id")
        var modelId: String,
        @SerializedName("ram")
        var ram: String,
        @SerializedName("storage")
        var storage: String
    )

    data class FinalPageInfo(
        @SerializedName("imei")
        var imei: String,
        @SerializedName("location")
        var location: String,
        @SerializedName("product_image")
        var productImage: String,
        @SerializedName("product_name")
        var productName: String,
        @SerializedName("product_original_value")
        var productOriginalValue: Double,
        @SerializedName("product_value")
        var productValue: Double,
        @SerializedName("store_icon")
        var storeIcon: String,
        @SerializedName("store_name")
        var storeName: String,
        @SerializedName("store_type")
        var storeType: String
    )
}