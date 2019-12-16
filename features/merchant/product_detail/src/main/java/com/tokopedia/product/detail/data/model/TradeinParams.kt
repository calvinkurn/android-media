package com.tokopedia.product.detail.data.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TradeinParams(
        @SerializedName("CategoryId")
        var categoryId: Int = 0,
        @SerializedName("DeviceId")
        var deviceId: String = "",
        @SerializedName("isEligible")
        var isEligible: Int = 0,
        @SerializedName("IsOnCampaign")
        var isOnCampaign: Boolean = false,
        @SerializedName("IsPreOrder")
        var isPreOrder: Boolean = false,
        @SerializedName("ModelId")
        var modelId: Int = 0,
        @SerializedName("NewPrice")
        var newPrice: Int = 0,
        @SerializedName("ProductId")
        var productId: Int = 0,
        @SerializedName("productName")
        var productName: String = "",
        @SerializedName("remainingPrice")
        var remainingPrice: Int = 0,
        @SerializedName("ShopId")
        var shopId: Int = 0,
        @SerializedName("TradeInType")
        var tradeInType: Int = 0,
        @SerializedName("useKyc")
        var useKyc: Int = 0,
        @SerializedName("usedPrice")
        var usedPrice: Int = 0,
        @SerializedName("UserId")
        var userId: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeString(deviceId)
        parcel.writeInt(isEligible)
        parcel.writeByte(if (isOnCampaign) 1 else 0)
        parcel.writeByte(if (isPreOrder) 1 else 0)
        parcel.writeInt(modelId)
        parcel.writeInt(newPrice)
        parcel.writeInt(productId)
        parcel.writeString(productName)
        parcel.writeInt(remainingPrice)
        parcel.writeInt(shopId)
        parcel.writeInt(tradeInType)
        parcel.writeInt(useKyc)
        parcel.writeInt(usedPrice)
        parcel.writeInt(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TradeinParams> {
        override fun createFromParcel(parcel: Parcel): TradeinParams {
            return TradeinParams(parcel)
        }

        override fun newArray(size: Int): Array<TradeinParams?> {
            return arrayOfNulls(size)
        }
    }
}