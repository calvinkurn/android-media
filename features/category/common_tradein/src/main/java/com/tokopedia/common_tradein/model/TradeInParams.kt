package com.tokopedia.common_tradein.model

import android.os.Parcel
import android.os.Parcelable

class TradeInParams(var productId : Int = 0,
                    var shopId : Int = 0,
                    var categoryId : Int = 0,
                    var userId : Int = 0,
                    var deviceId: String? = null,
                    var newPrice : Int = 0,
                    var isPreorder :Boolean = false,
                    var isOnCampaign :Boolean = false,
                    var tradeInType : Int = 0,
                    var modelID : Int = 0,
                    var widgetString: String? = null,
                    var productName: String? = null,
                    var usedPrice : Int = 0,
                    var remainingPrice : Int = 0,
                    var isUseKyc : Int = 0,
                    var isEligible : Int = 0,
                    var origin: String? = null,
                    var productImage: String? = null,
                    var weight : Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    fun setPrice(price: Int) {
        newPrice = price
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeInt(shopId)
        parcel.writeInt(categoryId)
        parcel.writeInt(userId)
        parcel.writeString(deviceId)
        parcel.writeInt(newPrice)
        parcel.writeByte(if (isPreorder) 1 else 0)
        parcel.writeByte(if (isOnCampaign) 1 else 0)
        parcel.writeInt(tradeInType)
        parcel.writeInt(modelID)
        parcel.writeString(widgetString)
        parcel.writeString(productName)
        parcel.writeInt(usedPrice)
        parcel.writeInt(remainingPrice)
        parcel.writeInt(isUseKyc)
        parcel.writeInt(isEligible)
        parcel.writeString(origin)
        parcel.writeString(productImage)
        parcel.writeInt(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TradeInParams> {
        override fun createFromParcel(parcel: Parcel): TradeInParams {
            return TradeInParams(parcel)
        }

        override fun newArray(size: Int): Array<TradeInParams?> {
            return arrayOfNulls(size)
        }
        val TRADE_IN_PARAMS = TradeInParams::class.java.simpleName
        const val PARAM_NEW_PRICE = "NEW PRICE"
        const val PARAM_DEVICE_ID = "DEVICE ID"
        const val PARAM_PHONE_TYPE = "PHONE TYPE"
        const val PARAM_PHONE_PRICE = "PHONE PRICE"
        const val PARAM_USER_ID = "USER ID"
        const val PARAM_PRODUCT_ID = "PRODUCT ID"
        const val PARAM_NEW_DEVICE_NAME = "NEW DEVICE NAME"
        const val PARAM_USE_KYC = "USE KYC"
        const val PARAM_PERMISSION_GIVEN = "PERMISSION GIVEN"
        const val HANDFONE_ID = 24
        const val HANDFONE = "Handphone"
    }
}