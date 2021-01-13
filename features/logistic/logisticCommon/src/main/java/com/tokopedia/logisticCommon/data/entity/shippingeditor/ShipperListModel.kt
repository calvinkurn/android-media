package com.tokopedia.logisticCommon.data.entity.shippingeditor


data class ShipperListModel(
        var shippers: ShippersModel = ShippersModel(),
        var ticker: List<TickerModel> = emptyList()
)

data class ShippersModel(
        var onDemand: List<OnDemandModel> = emptyList(),
        var conventional: List<ConventionalModel> = emptyList()
)


data class OnDemandModel(
        var shipperId: Int = -1,
        var name: String = "",
        var isActive: Boolean = false,
        var textPromo: String = "",
        var image: String = "",
        var featureInfo: List<FeatureInfoModel> = listOf(),
        var shipperProduct: List<ShipperProductModel> = listOf()
)

data class ConventionalModel(
        var shipperId: Int = -1,
        var name: String = "",
        var isActive: Boolean = false,
        var textPromo: String = "",
        var image: String = "",
        var featureInfo: List<FeatureInfoModel> = listOf(),
        var shipperProduct: List<ShipperProductModel> = listOf()
)

data class FeatureInfoModel(
        var header: String = "",
        var body: String = ""
)

data class ShipperProductModel(
        var shipperProductId: String = "",
        var shipperProductName: String = "",
        var isActive: Boolean = false
)

data class TickerModel(
        var header: String = "",
        var body: String = "",
        var textLink: String = "",
        var urlLink: String = ""
)
