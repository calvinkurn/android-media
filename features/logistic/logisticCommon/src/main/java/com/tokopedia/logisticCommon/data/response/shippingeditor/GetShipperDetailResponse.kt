package com.tokopedia.logisticCommon.data.response.shippingeditor

import com.google.gson.annotations.SerializedName

data class GetShipperDetailResponse(
        @SerializedName("ongkirShippingEditorGetShipperDetail")
        var ongkirShippingEditorGetShipperDetail: OngkirShippingEditorGetShipperDetail = OngkirShippingEditorGetShipperDetail()
)

data class OngkirShippingEditorGetShipperDetail(
        @SerializedName("status")
        var status: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("data")
        var data: DataShipperDetail = DataShipperDetail()
)

data class DataShipperDetail(
        @SerializedName("shipper_details")
        var shipperDetails: List<ShipperDetails> = listOf(),
        @SerializedName("feature_details")
        var featureDetails: List<FeatureDetails> = listOf(),
        @SerializedName("service_details")
        var serviceDetails: List<ServiceDetails> = listOf()
)

data class ShipperDetails(
        @SerializedName("name")
        var name: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("image")
        var image: String = "",
        @SerializedName("shipper_product")
        var shipperProduct: List<ShipperProductDetails> = listOf()
)

data class ShipperProductDetails(
        @SerializedName("name")
        var name: String = "",
        @SerializedName("description")
        var description: String = ""
)

data class FeatureDetails(
        @SerializedName("header")
        var header: String = "",
        @SerializedName("description")
        var description: String = ""
)

data class ServiceDetails(
        @SerializedName("header")
        var header: String = "",
        @SerializedName("description")
        var description: String = ""
)

