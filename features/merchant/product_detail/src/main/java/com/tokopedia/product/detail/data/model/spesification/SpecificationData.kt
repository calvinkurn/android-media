package com.tokopedia.product.detail.data.model.spesification

import android.os.Parcel
import android.os.Parcelable

data class SpecificationData(
        var listOfSpecification: MutableList<SpecificationItem> = mutableListOf()
)

data class SpecificationItem(
        var name: String = "",
        var row: MutableList<RowItem> = mutableListOf()
)

data class RowItem(
        var key: String = "",
        var value: ArrayList<String> = arrayListOf()
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: arrayListOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeStringList(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RowItem> {
        override fun createFromParcel(parcel: Parcel): RowItem {
            return RowItem(parcel)
        }

        override fun newArray(size: Int): Array<RowItem?> {
            return arrayOfNulls(size)
        }
    }
}