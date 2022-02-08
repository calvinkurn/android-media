package com.tokopedia.addongifting.data.response

import com.google.gson.annotations.SerializedName

data class GetAddOnSavedStateResponse(
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
        val addOnPrice: Double = 0.0,
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