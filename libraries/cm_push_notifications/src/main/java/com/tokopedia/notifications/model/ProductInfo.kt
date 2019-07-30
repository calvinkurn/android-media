package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductInfo(

        @SerializedName("productTitle")
        var productTitle: String,

        @SerializedName("productImg")
        var productImage: String,

        @SerializedName("actualPrice")
        var productActualPrice: String?,

        @SerializedName("currentPrice")
        var productCurrentPrice: String,

        @SerializedName("droppedPercent")
        var productPriceDroppedPercentage: String?,

        @SerializedName("message")
        var productMessage: String,

        @SerializedName("buttonTxt")
        var productButtonMessage: String,

        @SerializedName("appLink")
        var appLink: String
) : Parcelable{
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(productTitle)
                parcel.writeString(productImage)
                parcel.writeString(productActualPrice)
                parcel.writeString(productCurrentPrice)
                parcel.writeString(productPriceDroppedPercentage)
                parcel.writeString(productMessage)
                parcel.writeString(productButtonMessage)
                parcel.writeString(appLink)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<ProductInfo> {
                override fun createFromParcel(parcel: Parcel): ProductInfo {
                        return ProductInfo(parcel)
                }

                override fun newArray(size: Int): Array<ProductInfo?> {
                        return arrayOfNulls(size)
                }
        }

}
