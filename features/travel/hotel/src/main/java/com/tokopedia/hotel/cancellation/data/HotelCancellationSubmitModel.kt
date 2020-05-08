package com.tokopedia.hotel.cancellation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 08/05/20
 */

data class HotelCancellationSubmitModel(
        @SerializedName("success")
        @Expose
        val success: Boolean = false,

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("desc")
        @Expose
        val desc: String = "",

        @SerializedName("actionButton")
        @Expose
        val actionButton: List<ActionButton> = listOf()

) {
    data class ActionButton(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("buttonType")
            @Expose
            val buttonType: String = "",

            @SerializedName("URI")
            @Expose
            val uri: String = "",

            @SerializedName("URIWeb")
            @Expose
            val uriWeb: String = ""
    )

    data class Response(
            @SerializedName("data")
            @Expose
            val data: HotelCancellationSubmitModel = HotelCancellationSubmitModel(),

            @SerializedName("meta")
            @Expose
            val meta: HotelCancellationMeta
    )

    data class HotelCancellationMeta(
            @SerializedName("cancelCartID")
            @Expose
            val cancelCartId: String = "",

            @SerializedName("selectedReason")
            @Expose
            val selectedReason: SelectedReason = SelectedReason()
    )

    data class SelectedReason(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("freeText")
            @Expose
            val freeText: Boolean = false
    )
}