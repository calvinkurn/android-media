package com.tokopedia.product_bundle.common.data.model.request

import com.google.gson.annotations.SerializedName

data class GetBundleInfoRequest(
        @SerializedName("bundles")
        val bundles: List<Bundle> = listOf(),
        @SerializedName("squad")
        val squad: String = "",
        @SerializedName("usecase")
        val usecase: String = "",
        @SerializedName("requestData")
        val requestData: RequestData = RequestData()
)

data class Bundle(
        @SerializedName("ID")
        val id: String = "",
        @SerializedName("WarehouseID")
        val warehouseId: String = "",
        @SerializedName("Products")
        val products: List<Product> = listOf()
)

data class Product(
        @SerializedName("ProductID")
        val productID: String = "",
        @SerializedName("ChildIDs")
        val childIds: List<String> = listOf()
)

data class RequestData(
        @SerializedName("ProductDetail")
        val productDetail: ProductDetail = ProductDetail(),
        @SerializedName("InventoryDetail")
        val inventoryDetail: InventoryDetail = InventoryDetail(),
        @SerializedName("VariantDetail")
        val variantDetail: Boolean = false,
        @SerializedName("Campaign")
        val campaign: Campaign = Campaign()
)

data class ProductDetail(
        @SerializedName("Picture")
        val picture: Boolean = false,
        @SerializedName("Preorder")
        val preorder: Boolean = false
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