package com.tokopedia.logisticCommon.data.response.shippingeditor

import com.google.gson.annotations.SerializedName

data class ValidateShippingEditorResponse(
        @SerializedName("ongkirShippingEditorPopup")
        var ongkirShippingEditorPopup: OngkirShippingEditorPopup = OngkirShippingEditorPopup()
)

data class OngkirShippingEditorPopup(
        @SerializedName("status")
        var status: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("data")
        var data: DataShippingEditorPopup = DataShippingEditorPopup()
)

data class DataShippingEditorPopup(
        @SerializedName("state")
        var state: Int = 0,
        @SerializedName("ui_content")
        var uiContent: UiContent = UiContent(),
        @SerializedName("feature_id")
        var featureId: List<Int> = listOf()
)

data class UiContent(
        @SerializedName("header")
        var header: String = "",
        @SerializedName("body")
        var body: List<String> = listOf(),
        @SerializedName("ticker")
        var ticker: TickerContent = TickerContent(),
        @SerializedName("header_location")
        var headerLocation: String = "",
        @SerializedName("warehouses")
        var warehouses: List<Warehouses> = listOf(),
        @SerializedName("warehouse_ids")
        var warehouseId: List<Int> = listOf()

)

data class TickerContent(
        @SerializedName("header")
        var header: String = "",
        @SerializedName("body")
        var body: String = "",
        @SerializedName("text_link")
        var textLink: String = "",
        @SerializedName("url_link")
        var urlLink: String = ""
)
