package com.tokopedia.hotel.cancellation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 07/05/20
 */

data class HotelCancellationSubmitParam(
        @SerializedName("cancelCartID")
        @Expose
        val cancelCartId: String = "",

        @SerializedName("selectedReason")
        @Expose
        val selectedReason: SelectedReason = SelectedReason()
) {
    data class SelectedReason(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("customText")
            @Expose
            val customText: String = ""
    )
}