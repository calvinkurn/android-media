package com.tokopedia.product.detail.data.model.spesification


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Specification(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("row")
        val row: List<Row> = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.createTypedArrayList(Row.CREATOR) ?: listOf())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeTypedList(row)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Specification> {
        override fun createFromParcel(parcel: Parcel): Specification {
            return Specification(parcel)
        }

        override fun newArray(size: Int): Array<Specification?> {
            return arrayOfNulls(size)
        }
    }
}