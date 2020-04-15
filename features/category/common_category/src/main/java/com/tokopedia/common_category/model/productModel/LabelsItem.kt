package com.tokopedia.common_category.model.productModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LabelsItem(

        @field:SerializedName("color")
        val color: String? = null,

        @field:SerializedName("title")
        val title: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(color)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabelsItem> {
        override fun createFromParcel(parcel: Parcel): LabelsItem {
            return LabelsItem(parcel)
        }

        override fun newArray(size: Int): Array<LabelsItem?> {
            return arrayOfNulls(size)
        }
    }
}