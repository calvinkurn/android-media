package com.tokopedia.recharge_slice.data

import android.os.Parcel
import android.os.Parcelable

data class TrackingData(
        var user_id :String = "",
        var products : List<Product> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(Product))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeTypedList(products)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackingData> {
        override fun createFromParcel(parcel: Parcel): TrackingData {
            return TrackingData(parcel)
        }

        override fun newArray(size: Int): Array<TrackingData?> {
            return arrayOfNulls(size)
        }
    }
}

data class Product(
        var product_id : String = "",
        var product_name : String = "",
        var product_price : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(product_id)
        parcel.writeString(product_name)
        parcel.writeString(product_price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}