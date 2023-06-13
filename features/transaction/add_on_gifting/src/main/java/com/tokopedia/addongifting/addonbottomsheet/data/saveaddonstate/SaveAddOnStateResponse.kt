package com.tokopedia.addongifting.addonbottomsheet.data.saveaddonstate

import com.google.gson.annotations.SerializedName

data class SaveAddOnStateResponse(
        @SerializedName("save_add_ons")
        val saveAddOns: SaveAddOnsResponse = SaveAddOnsResponse()
)

data class SaveAddOnsResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: DataResponse = DataResponse()
)

data class DataResponse(
        @SerializedName("add_ons")
        val addOns: List<AddOnResponse> = emptyList()
)

data class AddOnResponse(
        @SerializedName("add_on_bottomsheet")
        val addOnBottomSheet: AddOnBottomSheetResponse = AddOnBottomSheetResponse(),
        @SerializedName("add_on_button")
        val addOnButton: AddOnButtonResponse = AddOnButtonResponse(),
        @SerializedName("add_on_data")
        val addOnData: List<AddOnDataResponse> = emptyList(),
        @SerializedName("add_on_key")
        val addOnKey: String = "",
        @SerializedName("add_on_level")
        val addOnLevel: String = "",
        @SerializedName("status")
        val status: Int = 0
)

data class AddOnBottomSheetResponse(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("header_title")
        val headerTitle: String = "",
        @SerializedName("products")
        val products: List<ProductResponse> = emptyList(),
        @SerializedName("ticker")
        val ticker: TickerResponse = TickerResponse()
)

data class AddOnButtonResponse(
        @SerializedName("action")
        val action: Int = 0,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("left_icon_url")
        val leftIconUrl: String = "",
        @SerializedName("right_icon_url")
        val rightIconUrl: String = "",
        @SerializedName("title")
        val title: String = ""
)

data class AddOnDataResponse(
        @SerializedName("add_on_id")
        val addOnId: String = "",
        @SerializedName("add_on_metadata")
        val addOnMetadata: AddOnMetadataResponse = AddOnMetadataResponse(),
        @SerializedName("add_on_price")
        val addOnPrice: Double = 0.0,
        @SerializedName("add_on_qty")
        val addOnQty: Int = 0
)

data class AddOnMetadataResponse(
        @SerializedName("add_on_note")
        val addOnNote: AddOnNoteResponse = AddOnNoteResponse()
)

data class AddOnNoteResponse(
        @SerializedName("from")
        val from: String = "",
        @SerializedName("is_custom_note")
        val isCustomNote: Boolean = false,
        @SerializedName("notes")
        val notes: String = "",
        @SerializedName("to")
        val to: String = ""
)

data class ProductResponse(
        @SerializedName("product_image_url")
        val productImageUrl: String = "",
        @SerializedName("product_name")
        val productName: String = ""
)

data class TickerResponse(
        @SerializedName("text")
        val text: String = ""
)
