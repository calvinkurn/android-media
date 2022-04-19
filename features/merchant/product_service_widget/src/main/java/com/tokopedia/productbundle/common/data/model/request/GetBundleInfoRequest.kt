package com.tokopedia.productbundle.common.data.model.request

import com.google.gson.annotations.SerializedName

data class Bundle(
    @SerializedName("ID")
    val ID: String = "",
    @SerializedName("WarehouseID")
    val WarehouseID: String = "",
    @SerializedName("Products")
    val Products: List<Product> = listOf()
)

data class Product(
    @SerializedName("ProductID")
    val ProductID: String = "",
    @SerializedName("ChildIDs")
    val ChildIDs: List<String> = listOf()
)

data class RequestData(
    @SerializedName("ProductDetail")
    val productDetail: ProductInfo = ProductInfo(),
    @SerializedName("InventoryDetail")
    val inventoryDetail: InventoryDetail = InventoryDetail(),
    @SerializedName("VariantDetail")
    val variantDetail: Boolean = false,
    @SerializedName("CheckCampaign")
    val CheckCampaign: Boolean = false,
    @SerializedName("BundleGroup")
    val BundleGroup: Boolean = false,
    @SerializedName("Preorder")
    val Preorder: Boolean = false
)

data class ProductInfo(
    @SerializedName("Picture")
    val picture: Boolean = true,
)

data class InventoryDetail(
    @SerializedName("Required")
    val required: Boolean = true,
    @SerializedName("UserLocation")
    val userLocation: UserLocation = UserLocation()
)

data class UserLocation(
    @SerializedName("AddressID")
    val addressId: String = "",
    @SerializedName("DistrictID")
    val districtID: String = "",
    @SerializedName("PostalCode")
    val postalCode: String = "",
    @SerializedName("Latlon")
    val latlon: String = ""
)

data class ProductData(
    @SerializedName("ProductID")
    val productID: String = "",
    @SerializedName("WarehouseIDs")
    val warehouseIDs: List<String> = emptyList()
)