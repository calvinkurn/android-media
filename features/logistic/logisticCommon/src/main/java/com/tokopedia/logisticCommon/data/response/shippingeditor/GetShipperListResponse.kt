package com.tokopedia.logisticCommon.data.response.shippingeditor

import com.google.gson.annotations.SerializedName

data class GetShipperListResponse (
        @SerializedName("ongkirShippingEditor")
        var ongkirShippingEditor: OngkirShippingEditor = OngkirShippingEditor()
)

data class OngkirShippingEditor(
        @SerializedName("status")
        var status: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("data")
        var data: Data = Data()
)

data class Data(
        @SerializedName("shippers")
        var shippers: Shippers = Shippers(),
        @SerializedName("ticker")
        var ticker: List<Ticker> = listOf()
)

data class Shippers(
        @SerializedName("ondemand")
        var onDemand: List<OnDemand> = listOf(),
        @SerializedName("conventional")
        var conventional: List<Conventional> = listOf()
)

data class OnDemand(
        @SerializedName("shipper_id")
        var shipperId: Int = -1,
        @SerializedName("shipper_name")
        var shipperName: String = "",
        @SerializedName("is_active")
        var isActive: Boolean = false,
        @SerializedName("text_promo")
        var textPromo: String = "",
        @SerializedName("image")
        var image: String = "",
        @SerializedName("feature_info")
        var featureInfo: List<FeatureInfo> = listOf(),
        @SerializedName("shipper_product")
        var shipperProduct: List<ShipperProduct> = listOf()
)

data class Conventional(
        @SerializedName("shipper_id")
        var shipperId: Int = -1,
        @SerializedName("shipper_name")
        var shipperName: String = "",
        @SerializedName("is_active")
        var isActive: Boolean = false,
        @SerializedName("text_promo")
        var textPromo: String = "",
        @SerializedName("image")
        var image: String = "",
        @SerializedName("feature_info")
        var featureInfo: List<FeatureInfo> = listOf(),
        @SerializedName("shipper_product")
        var shipperProduct: List<ShipperProduct> = listOf()
)

data class FeatureInfo(
        @SerializedName("header")
        var header: String = "",
        @SerializedName("body")
        var body: String = ""
)

data class ShipperProduct(
        @SerializedName("shipper_product_id")
        var shipperProductId: String = "",
        @SerializedName("shipper_product_name")
        var shipperProductName: String = "",
        @SerializedName("is_active")
        var isActive: Boolean = false
)

data class Ticker(
        @SerializedName("header")
        var header: String = "",
        @SerializedName("body")
        var body: String = "",
        @SerializedName("text_link")
        var textLink: String = "",
        @SerializedName("url_link")
        var urlLink: String = ""
)