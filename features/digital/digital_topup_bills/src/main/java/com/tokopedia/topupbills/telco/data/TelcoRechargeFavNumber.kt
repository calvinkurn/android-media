package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TelcoRechargeFavNumber(
        @SerializedName("list")
        @Expose
        val favNumberList: List<TelcoFavNumber>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(TelcoFavNumber))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(favNumberList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TelcoRechargeFavNumber> {
        override fun createFromParcel(parcel: Parcel): TelcoRechargeFavNumber {
            return TelcoRechargeFavNumber(parcel)
        }

        override fun newArray(size: Int): Array<TelcoRechargeFavNumber?> {
            return arrayOfNulls(size)
        }
    }
}