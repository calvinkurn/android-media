package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class PropertyImageItem(@SerializedName("isLogoPhoto")
                        @Expose
                        val isLogoPhoto: Boolean = false,
                        @SerializedName("urlSquare60")
                        @Expose
                        val urlSquare6: String = "",
                        @SerializedName("mainPhoto")
                        @Expose
                        val mainPhoto: Boolean = false,
                        @SerializedName("urlOriginal")
                        @Expose
                        val urlOriginal: String = "",
                        @SerializedName("urlMax300")
                        @Expose
                        val urlMax300: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isLogoPhoto) 1 else 0)
        parcel.writeString(urlSquare6)
        parcel.writeByte(if (mainPhoto) 1 else 0)
        parcel.writeString(urlOriginal)
        parcel.writeString(urlMax300)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PropertyImageItem> {
        override fun createFromParcel(parcel: Parcel): PropertyImageItem {
            return PropertyImageItem(parcel)
        }

        override fun newArray(size: Int): Array<PropertyImageItem?> {
            return arrayOfNulls(size)
        }
    }
}