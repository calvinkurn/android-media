package com.tokopedia.topads.dashboard.data.model.insightkey


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Table(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("tooltip")
        val tooltip: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(tooltip)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Table> {
        override fun createFromParcel(parcel: Parcel): Table {
            return Table(parcel)
        }

        override fun newArray(size: Int): Array<Table?> {
            return arrayOfNulls(size)
        }
    }
}