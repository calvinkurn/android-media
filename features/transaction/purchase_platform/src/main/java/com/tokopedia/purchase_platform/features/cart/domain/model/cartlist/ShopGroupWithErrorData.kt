package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

data class ShopGroupWithErrorData(
        var cartItemHolderDataList: List<CartItemHolderData> = emptyList(),
        var isError: Boolean = false,
        var errorLabel: String = "",
        var similarProductUrl: String? = null,
        var shopName: String = "",
        var shopId: String = "",
        var shopType: String = "",
        var cityName: String = "",
        var isGoldMerchant: Boolean = false,
        var isOfficialStore: Boolean = false,
        var shopBadge: String = "",
        var isFulfillment: Boolean = false,
        var fulfillmentName: String = "",
        var hasPromoList: Boolean = false,
        var cartString: String = "",

        var isWarning: Boolean = false,
        var warningTitle: String? = null,
        var warningDescription: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(CartItemHolderData.CREATOR) ?: emptyList(),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(cartItemHolderDataList)
        parcel.writeByte(if (isError) 1 else 0)
        parcel.writeString(errorLabel)
        parcel.writeString(similarProductUrl)
        parcel.writeString(shopName)
        parcel.writeString(shopId)
        parcel.writeString(shopType)
        parcel.writeString(cityName)
        parcel.writeByte(if (isGoldMerchant) 1 else 0)
        parcel.writeByte(if (isOfficialStore) 1 else 0)
        parcel.writeString(shopBadge)
        parcel.writeByte(if (isFulfillment) 1 else 0)
        parcel.writeString(fulfillmentName)
        parcel.writeByte(if (hasPromoList) 1 else 0)
        parcel.writeString(cartString)
        parcel.writeByte(if (isWarning) 1 else 0)
        parcel.writeString(warningTitle)
        parcel.writeString(warningDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopGroupWithErrorData> {
        override fun createFromParcel(parcel: Parcel): ShopGroupWithErrorData {
            return ShopGroupWithErrorData(parcel)
        }

        override fun newArray(size: Int): Array<ShopGroupWithErrorData?> {
            return arrayOfNulls(size)
        }
    }
}