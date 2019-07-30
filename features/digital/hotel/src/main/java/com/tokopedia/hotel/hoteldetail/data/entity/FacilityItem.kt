package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class FacilityItem(@SerializedName("id")
                   @Expose
                   val id: Int = 0,
                   @SerializedName("name")
                   @Expose
                   val name: String = "",
                   @SerializedName("icon")
                   @Expose
                   val icon: String = "",
                   @SerializedName("iconUrl")
                   @Expose
                   val iconUrl: String = "",
                   @SerializedName("availability")
                   @Expose
                   val availability: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(iconUrl)
        parcel.writeInt(availability)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FacilityItem> {
        override fun createFromParcel(parcel: Parcel): FacilityItem {
            return FacilityItem(parcel)
        }

        override fun newArray(size: Int): Array<FacilityItem?> {
            return arrayOfNulls(size)
        }
    }

}