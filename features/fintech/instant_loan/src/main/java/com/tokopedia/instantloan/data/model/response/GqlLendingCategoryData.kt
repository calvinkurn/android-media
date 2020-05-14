package com.tokopedia.instantloan.data.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GqlLendingCategoryData(
        @SerializedName("ID")
        var categoryId: Int,

        @SerializedName("Name")
        var categoryName: String? = "",

        @SerializedName("NameSlug")
        var categoryNameSlug: String? = "",

        @SerializedName("Icon")
        var categoryIconUrl: String? = "",

        var isSelected: Boolean = false
) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeString(categoryName)
        parcel.writeString(categoryNameSlug)
        parcel.writeString(categoryIconUrl)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GqlLendingCategoryData> {
        override fun createFromParcel(parcel: Parcel): GqlLendingCategoryData {
            return GqlLendingCategoryData(parcel)
        }

        override fun newArray(size: Int): Array<GqlLendingCategoryData?> {
            return arrayOfNulls(size)
        }
    }
}
