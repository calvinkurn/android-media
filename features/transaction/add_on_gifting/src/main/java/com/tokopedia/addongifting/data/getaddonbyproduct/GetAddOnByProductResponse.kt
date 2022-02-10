package com.tokopedia.addongifting.data.getaddonbyproduct

import com.google.gson.annotations.SerializedName

data class GetAddOnByProductResponse(
        @SerializedName("GetAddOnByProductResponse")
        val dataResponse: DataResponse = DataResponse()
)

data class DataResponse(
        @SerializedName("error")
        val error: ErrorResponse = ErrorResponse(),
        @SerializedName("StaticInfo")
        val staticInfo: StaticInfoResponse = StaticInfoResponse(),
        @SerializedName("AddOnByProductResponse")
        val addOnByProducts: List<AddOnByProductResponse> = emptyList()
)

data class ErrorResponse(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("reason")
        val reason: List<String> = emptyList(),
        @SerializedName("errorCode")
        val errorCode: String = ""
)

data class StaticInfoResponse(
        @SerializedName("InfoURL")
        val infoUrl: String = ""
)

data class AddOnByProductResponse(
        @SerializedName("ProductID")
        val productId: String = "",
        @SerializedName("WarehouseID")
        val warehouseId: String = "",
        @SerializedName("AddOnType")
        val addOnType: String = "",
        @SerializedName("HasAddOns")
        val hasAdOns: Boolean = false,
        @SerializedName("CouponText")
        val couponText: String = "",
        @SerializedName("Addons")
        val addOns: List<AddOnResponse> = emptyList()
)

data class AddOnResponse(
        @SerializedName("Basic")
        val basicInfo: BasicInfoResponse = BasicInfoResponse(),
        @SerializedName("Pictures")
        val pictures: List<PictureResponse> = emptyList(),
        @SerializedName("Inventory")
        val inventory: InventoryResponse = InventoryResponse(),
        @SerializedName("Warehouse")
        val warehouse: WarehouseResponse = WarehouseResponse(),
        @SerializedName("Shop")
        val shop: ShopResponse = ShopResponse()
)

data class BasicInfoResponse(
        @SerializedName("ID")
        val id: String = "",
        @SerializedName("ShopID")
        val shopId: String = "",
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("Rules")
        val rules: RulesResponse = RulesResponse(),
        @SerializedName("Metadata")
        val metadata: MetadataResponse = MetadataResponse(),
        @SerializedName("ProductAddOnType")
        val productAddOnType: String = "",
        @SerializedName("Status")
        val status: String = ""
)

data class RulesResponse(
        @SerializedName("MaxOrder")
        val maxOrder: Int = 0,
        @SerializedName("CustomNotes")
        val customNote: Boolean = false
)

data class MetadataResponse(
        @SerializedName("Pictures")
        val pictures: List<PictureResponse> = emptyList(),
        @SerializedName("NotesTemplate")
        val notesTemplate: String = ""
)

data class PictureResponse(
        @SerializedName("FilePath")
        val filePath: String = "",
        @SerializedName("FileName")
        val fileName: String = "",
        @SerializedName("URL")
        val url: String = "",
        @SerializedName("URL100")
        val url100: String = "",
        @SerializedName("URL200")
        val url200: String = ""
)

data class InventoryResponse(
        @SerializedName("WarehouseID")
        val warehouseId: String = "",
        @SerializedName("Price")
        val price: Double = 0.0,
        @SerializedName("Stock")
        val stock: Int = 0,
        @SerializedName("UnlimitedStock")
        val isUnlimitedStock: Boolean = true
)

data class WarehouseResponse(
        @SerializedName("WarehouseName")
        val warehouseName: String = "",
        @SerializedName("CityName")
        val cityName: String = ""
)

data class ShopResponse(
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("ShopTier")
        val shopTier: Int = 0,
        @SerializedName("ShopType")
        val shopType: Int = 0
)