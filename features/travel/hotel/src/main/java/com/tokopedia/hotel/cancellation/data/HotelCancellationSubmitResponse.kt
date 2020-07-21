package com.tokopedia.hotel.cancellation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 12/05/20
 */
data class HotelCancellationSubmitResponse(
        @SerializedName("hotelSubmitCancelRequest")
        @Expose
        val response: CancellationSubmitMetaAndData = CancellationSubmitMetaAndData())
{
    data class CancellationSubmitMetaAndData(
            @SerializedName("data")
            @Expose
            val data: HotelCancellationSubmitModel = HotelCancellationSubmitModel(),

            @SerializedName("meta")
            @Expose
            val meta: HotelCancellationMeta = HotelCancellationMeta()
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