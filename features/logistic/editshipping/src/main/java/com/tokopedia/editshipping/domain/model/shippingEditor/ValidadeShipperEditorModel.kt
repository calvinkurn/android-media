package com.tokopedia.editshipping.domain.model.shippingEditor

data class ValidateShippingEditorModel(
        var state: Int = 0,
        var uiContent: UiContentModel = UiContentModel(),
        var featureId: List<Int> = listOf()
)

data class UiContentModel(
        var header: String = "",
        var body: List<String> = listOf(),
        var ticker: TickerContentModel = TickerContentModel(),
        var headerLocation: String = "",
        var warehouses: List<WarehousesModel> = listOf(),
        var warehouseId: List<Int> = listOf()

)

data class TickerContentModel(
        var header: String = "",
        var body: String = "",
        var textLink: String = "",
        var urlLink: String = ""
)