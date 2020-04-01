package com.tokopedia.notifications.model

import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

data class ProductInfo(

        @SerializedName("productTitle")
        @ColumnInfo(name = "productTitle")
        @Expose
        var productTitle: String,

        @SerializedName("productImg")
        @ColumnInfo(name = "productImg")
        @Expose
        var productImage: String,

        @SerializedName("actualPrice")
        @ColumnInfo(name = "actualPrice")
        @Expose
        var productActualPrice: String?,

        @SerializedName("currentPrice")
        @ColumnInfo(name = "currentPrice")
        @Expose
        var productCurrentPrice: String,

        @SerializedName("droppedPercent")
        @ColumnInfo(name = "droppedPercent")
        @Expose
        var productPriceDroppedPercentage: String?,

        @SerializedName("message")
        @ColumnInfo(name = "message")
        @Expose
        var productMessage: String,

        @SerializedName("buttonTxt")
        @ColumnInfo(name = "buttonTxt")
        @Expose
        var productButtonMessage: String,

        @SerializedName("appLink")
        @ColumnInfo(name = "appLink")
        @Expose
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        @ColumnInfo(name = CMConstant.PayloadKeys.ELEMENT_ID)
        @Expose
        var element_id: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productTitle)
        parcel.writeString(productImage)
        parcel.writeString(productActualPrice)
        parcel.writeString(productCurrentPrice)
        parcel.writeString(productPriceDroppedPercentage)
        parcel.writeString(productMessage)
        parcel.writeString(productButtonMessage)
        parcel.writeString(appLink)
        parcel.writeString(element_id)
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
