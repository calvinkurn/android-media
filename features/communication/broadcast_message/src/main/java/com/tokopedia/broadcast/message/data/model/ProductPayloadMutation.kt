package com.tokopedia.broadcast.message.data.model

import android.os.Parcel
import android.os.Parcelable

data class ProductPayloadMutation(val productId: Int = -1,
                                  val productProfile: ProductProfileMutation): Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readParcelable(ProductProfileMutation::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeParcelable(productProfile, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductPayloadMutation> {
        override fun createFromParcel(parcel: Parcel): ProductPayloadMutation {
            return ProductPayloadMutation(parcel)
        }

        override fun newArray(size: Int): Array<ProductPayloadMutation?> {
            return arrayOfNulls(size)
        }
    }

    data class ProductProfileMutation(val name: String,
                                      val price: String,
                                      val imageUrl: String,
                                      val url: String): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(price)
            parcel.writeString(imageUrl)
            parcel.writeString(url)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ProductProfileMutation> {
            override fun createFromParcel(parcel: Parcel): ProductProfileMutation {
                return ProductProfileMutation(parcel)
            }

            override fun newArray(size: Int): Array<ProductProfileMutation?> {
                return arrayOfNulls(size)
            }
        }
    }
}