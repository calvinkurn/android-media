package com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct

import com.google.gson.annotations.SerializedName

data class GetAddOnByProductRequest(
        @SerializedName("AddOnRequest")
        var addOnRequest: List<AddOnByProductRequest> = emptyList(),
        @SerializedName("Source")
        var sourceRequest: SourceRequest = SourceRequest(),
        @SerializedName("RequestData")
        var requestData: RequestData = RequestData()
)

data class AddOnByProductRequest(
        @SerializedName("ProductID")
        var productId: String = "",
        @SerializedName("WarehouseID")
        var warehouseId: String = "",
        @SerializedName("AddOnLevel")
        var addOnLevel: String = ""
) {
    companion object {
        const val ADD_ON_LEVEL_PRODUCT = "PRODUCT_ADDON"
        const val ADD_ON_LEVEL_ORDER = "ORDER_ADDON"
    }
}

data class SourceRequest(
        @SerializedName("Squad")
        var squad: String = "",
        @SerializedName("Usecase")
        var useCase: String = ""
) {
    companion object {
        const val SQUAD_VALUE = "android_purchase_platform"
        const val USE_CASE_VALUE = "add_on_bottomsheet"
    }
}

data class RequestData(
        @SerializedName("Inventory")
        var inventory: Boolean = false
)