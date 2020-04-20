package com.tokopedia.topads.edit.data.param

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 14/4/20.
 */

class ProductData() : Parcelable {
    @field:SerializedName("price")
    var price: String? = ""
    @field:SerializedName("name")
    var name: String? = ""
    @field:SerializedName("id")
    var id: Int = 0
    @field:SerializedName("image")
    var image: String? = ""

    constructor(parcel: Parcel) : this() {
        price = parcel.readString()
        name = parcel.readString()
        id = parcel.readInt()
        image = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(price)
        parcel.writeString(name)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductData> {
        override fun createFromParcel(parcel: Parcel): ProductData {
            return ProductData(parcel)
        }

        override fun newArray(size: Int): Array<ProductData?> {
            return arrayOfNulls(size)
        }
    }

}