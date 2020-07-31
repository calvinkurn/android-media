package com.tokopedia.topads.dashboard.data.model.insightkey


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Keyword(
        @SerializedName("id")
        val id: String? = "",
        @SerializedName("priceBid")
        val priceBid: Int = 0,
        @SerializedName("source")
        val source: String? = "",
        @SerializedName("status")
        val status: String? = "",
        @SerializedName("tag")
        val tag: String? = "",
        @SerializedName("type")
        val type: String? = ""
): Serializable,Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(priceBid)
        parcel.writeString(source)
        parcel.writeString(status)
        parcel.writeString(tag)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Keyword> {
        override fun createFromParcel(parcel: Parcel): Keyword {
            return Keyword(parcel)
        }

        override fun newArray(size: Int): Array<Keyword?> {
            return arrayOfNulls(size)
        }
    }
}