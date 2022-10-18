package com.tokopedia.logisticCommon.data.response.customproductlogistic

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class OngkirGetCPLQGLResponse (
    @SerializedName("ongkirGetCPLEditor")
    var response: OngkirGetCPLResponse = OngkirGetCPLResponse()
)

data class OngkirGetCPLResponse (
    @SerializedName("data")
    var data: GetCPLData = GetCPLData()
)

data class GetCPLData (
    @SerializedName("cpl_product")
    @Deprecated("use shipper_product.is_active")
    var cplProduct: List<CPLProduct> = listOf(),
    @SerializedName("shipper_list")
    var shipperList: List<ShipperList> = listOf()
)

@Deprecated("use shipper_product.is_active")
data class CPLProduct(
    @SuppressLint("Invalid Data Type")
    @SerializedName("product_id")
    var productId: Long = 0,
    @SerializedName("cpl_status")
    var cplStatus: Int = 0,
    @SerializedName("shipper_services")
    var shipperServices: List<Long> = listOf()
)

data class ShipperList(
    @SerializedName("header")
    var header: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("shippers")
    var shipper: List<Shipper> = listOf(),
    @SerializedName("whitelabels")
    var whitelabelShipper: List<WhitelabelShipper> = listOf()
)

data class WhitelabelShipper(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("shipper_product_ids")
    var shipperProductIds: List<Long> = listOf(),
    @SerializedName("is_active")
    var isActive: Boolean = false
)

data class Shipper(
    @SuppressLint("Invalid Data Type")
    @SerializedName("shipper_id")
    var shipperId: Long = 0,
    @SerializedName("shipper_name")
    var shipperName: String = "",
    @SerializedName("logo")
    var logo: String = "",
    @SerializedName("shipper_product")
    var shipperProduct: List<ShipperProduct> = listOf()
)

data class ShipperProduct(
    @SuppressLint("Invalid Data Type")
    @SerializedName("shipper_product_id")
    var shipperProductId: Long = 0,
    @SerializedName("shipper_product_name")
    var shipperProductName: String = "",
    @SerializedName("ui_hidden")
    var uiHidden: Boolean = false,
    @SerializedName("is_active")
    var isActive: Boolean = false
)
