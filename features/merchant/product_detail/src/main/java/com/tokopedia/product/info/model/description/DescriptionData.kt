package com.tokopedia.product.info.model.description

import android.os.Parcel
import android.os.Parcelable

data class DescriptionData(
        var basicId:  String = "",

        var basicName: String = "",

        var basicPrice: Float = 0f,

        var shopName: String = "",

        var thumbnailPicture: String = "",

        var basicDescription: String = "",

        var videoUrlList: List<String> = listOf(),

        var isOfficial: Boolean = false,

        var isGoldMerchant: Boolean = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: listOf(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(basicId)
        parcel.writeString(basicName)
        parcel.writeFloat(basicPrice)
        parcel.writeString(shopName)
        parcel.writeString(thumbnailPicture)
        parcel.writeString(basicDescription)
        parcel.writeStringList(videoUrlList)
        parcel.writeByte(if (isOfficial) 1 else 0)
        parcel.writeByte(if (isGoldMerchant) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DescriptionData> {
        override fun createFromParcel(parcel: Parcel): DescriptionData {
            return DescriptionData(parcel)
        }

        override fun newArray(size: Int): Array<DescriptionData?> {
            return arrayOfNulls(size)
        }
    }
}