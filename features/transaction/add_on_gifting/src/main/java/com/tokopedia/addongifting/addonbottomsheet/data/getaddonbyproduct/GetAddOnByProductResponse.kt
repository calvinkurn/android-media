package com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct

import com.google.gson.annotations.SerializedName

data class GetAddOnByProductResponse(
        @SerializedName("GetAddOnByProduct")
        val dataResponse: DataResponse = DataResponse()
)

data class DataResponse(
        @SerializedName("error")
        val error: ErrorResponse = ErrorResponse(),
        @SerializedName("StaticInfo")
        val staticInfo: StaticInfo = StaticInfo(),
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

data class AddOnByProductResponse(
        @SerializedName("ProductID")
        val productId: String = "",
        @SerializedName("WarehouseID")
        val warehouseId: String = "",
        @SerializedName("AddOnType")
        val addOnType: String = "",
        @SerializedName("HasAddOns")
        val hasAdOns: Boolean = false,
        @SerializedName("Addons")
        val addOns: List<AddOnResponse> = emptyList()
)

data class AddOnResponse(
        @SerializedName("Basic")
        val basicInfo: BasicInfoResponse = BasicInfoResponse(),
        @SerializedName("Inventory")
        val inventory: InventoryResponse = InventoryResponse()
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
) {
        companion object {
                const val ADD_ON_TYPE_GREETING_CARD = "GREETING_CARD_TYPE"
                const val ADD_ON_TYPE_GREETING_CARD_AND_PACKAGING = "GREETING_CARD_AND_PACKAGING_TYPE"
        }
}

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

data class StaticInfo(
        @SerializedName("PromoText")
        val promoText: String = ""
)