package com.tokopedia.activation.model.response

import com.google.gson.annotations.SerializedName

class ShippingEditorResponse(
    @SerializedName("kero_get_shipping_editor")
    var keroGetShippingEditor: keroGetShippingEditor = keroGetShippingEditor()
)

data class keroGetShippingEditor(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("config")
    var config: String = "",
    @SerializedName("server_process_time")
    var serverProcessTime: String = "",
    @SerializedName("data")
    var data: Data = Data()
)

data class Data(
    @SerializedName("activated_shipping")
    var activatedShipping: ActivatedShipping = ActivatedShipping()
)

data class ActivatedShipping(
    @SerializedName("11")
    var x11: DetailShipping = DetailShipping()
)

data class DetailShipping(
    @SerializedName("Code")
    var code: String = "",
    @SerializedName("Image")
    var image: String = "",
    @SerializedName("IsAvailable")
    var isAvailable: Int = 0,
    @SerializedName("Name")
    var name: String = "",
    @SerializedName("Product")
    var product: List<Product> = listOf(),
    @SerializedName("ShipmentID")
    var shipmentID: Int = 0
)

data class Product(
    @SerializedName("IsAvailable")
    var isAvailable: Int = 0,
    @SerializedName("ProductName")
    var productName: String = "",
    @SerializedName("ShipProdID")
    var shipProdID: Int = 0,
    @SerializedName("UIHidden")
    var uIHidden: Boolean = false
)
