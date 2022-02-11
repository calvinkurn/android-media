package com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate

import com.google.gson.annotations.SerializedName

data class GetAddOnSavedStateResponse(
        @SerializedName("get_add_ons")
        val getAddOns: GetAddOnsResponse = GetAddOnsResponse()
)

data class GetAddOnsResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: DataResponse = DataResponse()
)

data class DataResponse(
        @SerializedName("add_ons")
        val addOns: List<AddOnSavedStateResponse> = emptyList()
)

data class AddOnSavedStateResponse(
        @SerializedName("add_on_key")
        val addOnKey: String = "",
        @SerializedName("add_on_level")
        val addOnLevel: String = "",
        @SerializedName("add_on_data")
        val addOnData: List<AddOnDataResponse> = emptyList()
)

data class AddOnDataResponse(
        @SerializedName("add_on_id")
        val addOnId: String = "",
        @SerializedName("add_on_qty")
        val addOnQty: Int = 0,
        @SerializedName("add_on_price")
        val addOnPrice: Long = 0,
        @SerializedName("add_on_metadata")
        val addOnMetadata: AddOnMetadataResponse = AddOnMetadataResponse()
)

data class AddOnMetadataResponse(
        @SerializedName("add_on_note")
        val addOnNote: AddOnNoteResponse = AddOnNoteResponse()
)

data class AddOnNoteResponse(
        @SerializedName("is_custom_note")
        val isCustomNote: Boolean = false,
        @SerializedName("to")
        val to: String = "",
        @SerializedName("from")
        val from: String = "",
        @SerializedName("notes")
        val notes: String = ""
)