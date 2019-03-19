package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 19/03/19.
 */

data class PromoStackingRequestData(
        var productId : Int = 0,
        var qty: Int = 0,
        var promoMerchantList: ArrayList<String> = ArrayList(),
        var shopId: Int = 0,
        var uniqueId: String = "",
        var promoGlobalCode: String = "",
        var skipApply: Int = 0,
        var isSuggested: Int = 0,
        var cartType: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            arrayListOf<String>().apply {
                parcel.readList(this, String::class.java.classLoader)
            },
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeInt(qty)
        parcel.writeInt(shopId)
        parcel.writeString(uniqueId)
        parcel.writeString(promoGlobalCode)
        parcel.writeInt(skipApply)
        parcel.writeInt(isSuggested)
        parcel.writeString(cartType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoStackingRequestData> {
        override fun createFromParcel(parcel: Parcel): PromoStackingRequestData {
            return PromoStackingRequestData(parcel)
        }

        override fun newArray(size: Int): Array<PromoStackingRequestData?> {
            return arrayOfNulls(size)
        }
    }
}