package com.tokopedia.hotel.cancellation.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 08/05/20
 */

@Parcelize
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

): Parcelable {

    @Parcelize
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
    ): Parcelable
}