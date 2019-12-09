package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Irfan Khoirul on 09/09/18.
 */

data class UpdateAndRefreshCartListData(
        var updateCartData: UpdateCartData? = null,
        var cartListData: CartListData? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(UpdateCartData::class.java.classLoader),
            parcel.readParcelable(CartListData::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(updateCartData, flags)
        parcel.writeParcelable(cartListData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UpdateAndRefreshCartListData> {
        override fun createFromParcel(parcel: Parcel): UpdateAndRefreshCartListData {
            return UpdateAndRefreshCartListData(parcel)
        }

        override fun newArray(size: Int): Array<UpdateAndRefreshCartListData?> {
            return arrayOfNulls(size)
        }
    }

}
