package com.tokopedia.product.detail.data.model.spesification


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Row(
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("value")
        @Expose
        val value: List<String> = listOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: listOf())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(key)
        dest.writeStringList(value)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Row> {
        override fun createFromParcel(parcel: Parcel): Row {
            return Row(parcel)
        }

        override fun newArray(size: Int): Array<Row?> {
            return arrayOfNulls(size)
        }
    }
}