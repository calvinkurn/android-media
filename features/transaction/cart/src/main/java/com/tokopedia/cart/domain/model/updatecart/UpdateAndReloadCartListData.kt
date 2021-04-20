package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.cart.domain.model.cartlist.CartListData

/**
 * @author Irfan Khoirul on 09/09/18.
 */

data class UpdateAndReloadCartListData(
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

    companion object CREATOR : Parcelable.Creator<UpdateAndReloadCartListData> {
        override fun createFromParcel(parcel: Parcel): UpdateAndReloadCartListData {
            return UpdateAndReloadCartListData(parcel)
        }

        override fun newArray(size: Int): Array<UpdateAndReloadCartListData?> {
            return arrayOfNulls(size)
        }
    }

}
