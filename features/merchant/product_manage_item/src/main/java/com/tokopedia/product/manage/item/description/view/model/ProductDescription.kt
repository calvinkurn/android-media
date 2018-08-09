package com.tokopedia.product.manage.item.description.view.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class ProductDescription(var description: String = "", var feature : String = "", var isNew : Boolean = true, var videoIDs : ArrayList<String> = ArrayList()): Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            arrayListOf<String>().apply {
                parcel.readList(this, String::class.java.classLoader)
            })

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