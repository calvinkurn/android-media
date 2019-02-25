package com.tokopedia.flight.search.data.api.single.response

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StopDetailEntity(
        @SerializedName("code")
        @Expose
        val code: String,
        @SerializedName("city")
        @Expose
        val city: String)
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StopDetailEntity> {
        override fun createFromParcel(parcel: Parcel): StopDetailEntity {
            return StopDetailEntity(parcel)
        }

        override fun newArray(size: Int): Array<StopDetailEntity?> {
            return arrayOfNulls(size)
        }
    }
}
