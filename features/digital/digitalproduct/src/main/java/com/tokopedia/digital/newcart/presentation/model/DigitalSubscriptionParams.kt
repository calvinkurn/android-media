package com.tokopedia.digital.newcart.presentation.model

import android.os.Parcel
import android.os.Parcelable

class DigitalSubscriptionParams(
        var showSubscribePopUp: String? = null,
        var autoSubscribe: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        showSubscribePopUp = parcel.readString()
        autoSubscribe = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(showSubscribePopUp)
        parcel.writeString(autoSubscribe)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalSubscriptionParams> {
        const val ARG_SHOW_SUBSCRIBE_POP_UP = "show_subscribe_pop_up"
        const val ARG_AUTO_SUBSCRIBE = "auto_subscribe"

        override fun createFromParcel(parcel: Parcel): DigitalSubscriptionParams {
            return DigitalSubscriptionParams(parcel)
        }

        override fun newArray(size: Int): Array<DigitalSubscriptionParams?> {
            return arrayOfNulls(size)
        }
    }
}