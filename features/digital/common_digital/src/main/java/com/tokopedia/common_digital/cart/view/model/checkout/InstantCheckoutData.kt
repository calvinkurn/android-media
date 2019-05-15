package com.tokopedia.common_digital.cart.view.model.checkout

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 3/23/17.
 */

class InstantCheckoutData(
        var successCallbackUrl: String? = null,
        var failedCallbackUrl: String? = null,
        var redirectUrl: String? = null,
        var transactionId: String? = null,
        var stringQuery: String? = null,
        var thanksUrl: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(successCallbackUrl)
        parcel.writeString(failedCallbackUrl)
        parcel.writeString(redirectUrl)
        parcel.writeString(transactionId)
        parcel.writeString(stringQuery)
        parcel.writeString(thanksUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InstantCheckoutData> {
        override fun createFromParcel(parcel: Parcel): InstantCheckoutData {
            return InstantCheckoutData(parcel)
        }

        override fun newArray(size: Int): Array<InstantCheckoutData?> {
            return arrayOfNulls(size)
        }
    }
}
