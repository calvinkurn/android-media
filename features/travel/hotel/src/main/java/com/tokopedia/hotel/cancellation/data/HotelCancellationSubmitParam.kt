package com.tokopedia.hotel.cancellation.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 07/05/20
 */

@Parcelize
data class HotelCancellationSubmitParam(
        @SerializedName("cancelCartID")
        @Expose
        val cancelCartId: String = "",

        @SerializedName("selectedReason")
        @Expose
        val selectedReason: SelectedReason = SelectedReason()
): Parcelable {
    @Parcelize
    data class SelectedReason(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("customText")
            @Expose
            val customText: String = ""
    ): Parcelable
}