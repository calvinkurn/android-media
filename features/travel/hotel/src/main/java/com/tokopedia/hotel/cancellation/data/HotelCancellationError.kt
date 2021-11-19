package com.tokopedia.hotel.cancellation.data

/**
 * @author by astidhiyaa on 04/10/21
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HotelCancellationError(
    @Expose
    @SerializedName("content")
    val content: Content = Content()
){
    data class Content(
        @Expose
        @SerializedName("actionButton")
        val actionButton: List<ActionButton> = listOf(),

        @Expose
        @SerializedName("desc")
        val desc: String = "",

        @Expose
        @SerializedName("success")
        val success: Boolean = false,

        @Expose
        @SerializedName("title")
        val title: String = ""
    )

    data class ActionButton(
        @Expose
        @SerializedName("buttonType")
        val buttonType: String = "",

        @Expose
        @SerializedName("label")
        val label: String = "",

        @Expose
        @SerializedName("URI")
        val uri: String = "",

        @Expose
        @SerializedName("URIWeb")
        val uriWeb: String = ""
    )
}