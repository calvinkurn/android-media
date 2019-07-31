package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class RegionData(@SerializedName("id")
                 @Expose
                 val id: Int = 0,
                 @SerializedName("name")
                 @Expose
                 val name: String = "",
                 @SerializedName("countryName")
                 @Expose
                 val countryName: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(countryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegionData> {
        override fun createFromParcel(parcel: Parcel): RegionData {
            return RegionData(parcel)
        }

        override fun newArray(size: Int): Array<RegionData?> {
            return arrayOfNulls(size)
        }
    }
}