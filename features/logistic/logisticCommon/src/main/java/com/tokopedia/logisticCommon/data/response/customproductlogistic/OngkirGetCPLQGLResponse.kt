package com.tokopedia.logisticCommon.data.response.customproductlogistic

import com.google.gson.annotations.SerializedName

data class OngkirGetCPLQGLResponse (
    @SerializedName("ongkirGetCPL")
    var response: OngkirGetCPLResponse = OngkirGetCPLResponse()
)

data class OngkirGetCPLResponse (
    @SerializedName("data")
    var data: GetCPLData = GetCPLData()
)

data class GetCPLData (
    @SerializedName("cpl_product")
    var cplProduct: CPLProduct = CPLProduct(),
    @SerializedName("shipper_list")
    var shipperList: ShipperList = ShipperList()
)

data class CPLProduct(
    @SerializedName("product_id")
    var productId: Long = 0,
    @SerializedName("cpl_status")
    var cplStatus: Int = 0,
    @SerializedName("shipper_services")
    var shipperServices: List<Int> = listOf()
)

data class ShipperList(
    @SerializedName("header")
    var header: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("shipper")
    var shipper: Shipper = Shipper()
)

data class Shipper(
    @SerializedName("shipper_id")
    var shipperId: Long = 0,
    @SerializedName("shipper_name")
    var shipperName: String = "",
    @SerializedName("logo")
    var logo: String = "",
    @SerializedName("shipper_product")
    var shipperProduct: ShipperProduct = ShipperProduct()
)

data class ShipperProduct(
    @SerializedName("shipper_product_id")
    var shipperProductId: Long = 0,
    @SerializedName("shipper_product_name")
    var shipperProductName: String = "",
    @SerializedName("ui_hidden")
    var uiHidden: Boolean = false
)