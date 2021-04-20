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
        @SerializedName("1")
        var x1: DetailShipping1 = DetailShipping1(),
        @SerializedName("2")
        var x2: DetailShipping2 = DetailShipping2(),
        @SerializedName("4")
        var x4: DetailShipping4 = DetailShipping4(),
        @SerializedName("6")
        var x6: DetailShipping6 = DetailShipping6(),
        @SerializedName("10")
        var x10: DetailShipping10 = DetailShipping10(),
        @SerializedName("11")
        var x11: DetailShipping11 = DetailShipping11(),
        @SerializedName("12")
        var x12: DetailShipping12 = DetailShipping12(),
        @SerializedName("13")
        var x13: DetailShipping13 = DetailShipping13(),
        @SerializedName("14")
        var x14: DetailShipping14 = DetailShipping14(),
        @SerializedName("16")
        var x16: DetailShipping16 = DetailShipping16(),
        @SerializedName("23")
        var x23: DetailShipping23 = DetailShipping23(),
        @SerializedName("24")
        var x24: DetailShipping24 = DetailShipping24()
)

data class DetailShipping1(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product1> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product1(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping2(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product2> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product2(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping4(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product4> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product4(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping6(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product6> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product6(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping10(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product10> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product10(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping11(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product11> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product11(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping12(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product12> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product12(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping13(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product13> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product13(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping14(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product14> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product14(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping16(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product16> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product16(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping23(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product23> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product23(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)

data class DetailShipping24(
        @SerializedName("Code")
        var code: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("Product")
        var product: List<Product24> = listOf(),
        @SerializedName("ShipmentID")
        var shipmentID: Int = 0
)

data class Product24(
        @SerializedName("IsAvailable")
        var isAvailable: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ShipProdID")
        var shipProdID: Int = 0,
        @SerializedName("UIHidden")
        var uIHidden: Boolean = false
)