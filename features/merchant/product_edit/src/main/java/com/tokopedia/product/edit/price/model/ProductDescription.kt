package com.tokopedia.product.edit.price.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

class ProductDescription(): Parcelable{
    var description: String? = null
    var feature: String? = null
    var isNew: Boolean = true
    var videoIDs: ArrayList<String> = ArrayList()

    constructor(parcel: Parcel) : this() {
        description = parcel.readString()
        feature = parcel.readString()
        isNew = parcel.readByte() != 0.toByte()
        videoIDs = arrayListOf<String>().apply {
                        parcel.readList(this, String::class.java.classLoader)
                    }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(feature)
        parcel.writeByte(if (isNew) 1 else 0)
        parcel.writeList(videoIDs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductDescription> {
        override fun createFromParcel(parcel: Parcel): ProductDescription {
            return ProductDescription(parcel)
        }

        override fun newArray(size: Int): Array<ProductDescription?> {
            return arrayOfNulls(size)
        }
    }
}