package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class DistrictData(@SerializedName("id")
                   @Expose
                   val id: Int = 0,
                   @SerializedName("cityId")
                   @Expose
                   val cityId: Int = 0,
                   @SerializedName("latitude")
                   @Expose
                   val latitude: Double = 0.0,
                   @SerializedName("longitude")
                   @Expose
                   val longitude: Double = 0.0,
                   @SerializedName("name")
                   @Expose
                   val name: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(cityId)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DistrictData> {
        override fun createFromParcel(parcel: Parcel): DistrictData {
            return DistrictData(parcel)
        }

        override fun newArray(size: Int): Array<DistrictData?> {
            return arrayOfNulls(size)
        }
    }
}