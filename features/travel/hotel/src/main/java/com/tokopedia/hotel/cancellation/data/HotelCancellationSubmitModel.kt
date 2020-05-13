package com.tokopedia.hotel.cancellation.data

import android.os.Parcel
import android.os.Parcelable
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

): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(ActionButton)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (success) 1 else 0)
        parcel.writeString(title)
        parcel.writeString(desc)
        parcel.writeTypedList(actionButton)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelCancellationSubmitModel> {
        override fun createFromParcel(parcel: Parcel): HotelCancellationSubmitModel {
            return HotelCancellationSubmitModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelCancellationSubmitModel?> {
            return arrayOfNulls(size)
        }
    }

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
    ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(label)
            parcel.writeString(buttonType)
            parcel.writeString(uri)
            parcel.writeString(uriWeb)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ActionButton> {
            override fun createFromParcel(parcel: Parcel): ActionButton {
                return ActionButton(parcel)
            }

            override fun newArray(size: Int): Array<ActionButton?> {
                return arrayOfNulls(size)
            }
        }
    }
}