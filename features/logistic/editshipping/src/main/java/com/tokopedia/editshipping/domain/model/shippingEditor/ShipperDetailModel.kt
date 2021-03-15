package com.tokopedia.editshipping.domain.model.shippingEditor

sealed class ShipperDetailVisitable

data class ShipperDetailModel(
        var shipperDetails: List<ShipperDetailsModel> = listOf(),
        var featureDetails: List<FeatureDetailsModel> = listOf(),
        var serviceDetails: List<ServiceDetailsModel> = listOf()
)

data class ShipperDetailsModel(
        var name: String = "",
        var description: String = "",
        var image: String = "",
        var shipperProduct: List<ShipperProductDetailsModel> = listOf()
) : ShipperDetailVisitable()

data class ShipperProductDetailsModel(
        var name: String = "",
        var description: String = ""
)

data class FeatureDetailsModel(
        var header: String = "",
        var description: String = ""
) : ShipperDetailVisitable()

data class ServiceDetailsModel(
        var header: String = "",
        var description: String = ""
) : ShipperDetailVisitable()

data class DividerModelFeature(var text: String? = null) : ShipperDetailVisitable()
data class DividerServiceFeature(var text: String? = null) : ShipperDetailVisitable()