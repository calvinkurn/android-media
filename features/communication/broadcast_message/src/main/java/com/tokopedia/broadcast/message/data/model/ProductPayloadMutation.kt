package com.tokopedia.broadcast.message.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductPayloadMutation(
        @SerializedName("productId")
        @Expose
        val productId: Int = -1,
        @SerializedName("productProfile")
        @Expose
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

    data class ProductProfileMutation(
            @SerializedName("name")
            @Expose
            val name: String,
            @SerializedName("price")
            @Expose
            val price: String,
            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String,
            @SerializedName("url")
            @Expose
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