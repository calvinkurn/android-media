package com.tokopedia.product_bundle.common.data.model.request

import com.google.gson.annotations.SerializedName

data class GetProductBundleRequest(
        @SerializedName("Bundles")
        val bundles: List<Bundle> = listOf(),
        @SerializedName("RequestData")
        val requestData: RequestData = RequestData(),
        @SerializedName("Source")
        val source: Source = Source()
)

data class Source(
        @SerializedName("Squad")
        val squad: String = "",
        @SerializedName("Usecase")
        val usecase: String = ""
)

data class Bundle(
        @SerializedName("ID")
        val id: Long = 0L,
        @SerializedName("WarehouseID")
        val warehouseId: Long = 0L,
        @SerializedName("Products")
        val products: List<Product> = listOf()
)

data class Product(
        @SerializedName("ProductID")
        val productID: Long = 0L,
        @SerializedName("ChildIDs")
        val childIds: List<Long> = listOf()
)

data class RequestData(
        @SerializedName("ProductDetail")
        val requestProductDetail: Boolean = false,
        @SerializedName("InventoryDetail")
        val inventoryDetail: InventoryDetail = InventoryDetail()
)

data class InventoryDetail(
        @SerializedName("Required")
        val required: Boolean = false,
        @SerializedName("UserLocation")
        val userLocation: UserLocation = UserLocation()
)

data class UserLocation(
        @SerializedName("AddressID")
        val addressId: Long = 0L,
        @SerializedName("DistrictID")
        val districtID: Long = 0L,
        @SerializedName("PostalCode")
        val postalCode: String = "",
        @SerializedName("Latlon")
        val latlon: String = ""
)