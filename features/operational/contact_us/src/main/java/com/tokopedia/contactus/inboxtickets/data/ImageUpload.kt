package com.tokopedia.contactus.inboxtickets.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.contactus.R

data class ImageUpload(
    var imageId: String? = null,
    var fileLoc: String? = null,
    var picSrc: String? = null,
    val picSrcLarge: String? = null,
    var picObj: String? = null,
    var position: Int = 0,
    val description: String? = null,
    val isSelected: Boolean = false,
    var imgSrc: Int = R.drawable.ic_upload
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageId)
        parcel.writeString(fileLoc)
        parcel.writeString(picSrc)
        parcel.writeString(picSrcLarge)
        parcel.writeString(picObj)
        parcel.writeInt(position)
        parcel.writeString(description)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeInt(imgSrc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageUpload> {
        override fun createFromParcel(parcel: Parcel): ImageUpload {
            return ImageUpload(parcel)
        }

        override fun newArray(size: Int): Array<ImageUpload?> {
            return arrayOfNulls(size)
        }
    }
}
