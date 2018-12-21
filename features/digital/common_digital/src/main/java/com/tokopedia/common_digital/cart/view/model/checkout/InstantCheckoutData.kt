package com.tokopedia.common_digital.cart.view.model.checkout

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 3/23/17.
 */

class InstantCheckoutData : Parcelable {

    var successCallbackUrl: String? = null
    var failedCallbackUrl: String? = null
    var redirectUrl: String? = null
    var transactionId: String? = null
    var stringQuery: String? = null


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.successCallbackUrl)
        dest.writeString(this.failedCallbackUrl)
        dest.writeString(this.redirectUrl)
        dest.writeString(this.transactionId)
        dest.writeString(this.stringQuery)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.successCallbackUrl = `in`.readString()
        this.failedCallbackUrl = `in`.readString()
        this.redirectUrl = `in`.readString()
        this.transactionId = `in`.readString()
        this.stringQuery = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<InstantCheckoutData> {
        override fun createFromParcel(source: Parcel): InstantCheckoutData {
            return InstantCheckoutData(source)
        }

        override fun newArray(size: Int): Array<InstantCheckoutData?> {
            return arrayOfNulls(size)
        }
    }
}
