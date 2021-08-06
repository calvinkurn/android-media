package com.tokopedia.product_bundle.common.data.model.request

import com.google.gson.annotations.SerializedName

data class RequestData(
        @SerializedName("ProductDetail")
        val productDetail: ProductInfo = ProductInfo(),
        @SerializedName("InventoryDetail")
        val inventoryDetail: InventoryDetail = InventoryDetail(),
        @SerializedName("VariantDetail")
        val variantDetail: Boolean = false,
        @SerializedName("Campaign")
        val campaign: Campaign = Campaign()
)

data class ProductInfo(
        @SerializedName("Picture")
        val picture: Boolean = false,
)

data class Campaign(
        @SerializedName("CheckCampaign")
        val checkCampaign: Boolean = false
)

data class InventoryDetail(
        @SerializedName("Required")
        val required: Boolean = false,
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
        val WarehouseIDs: List<String> = listOf()
)