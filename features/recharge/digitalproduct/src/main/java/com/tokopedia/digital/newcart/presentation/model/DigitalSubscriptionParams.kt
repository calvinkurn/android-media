package com.tokopedia.digital.newcart.presentation.model

import android.os.Parcel
import android.os.Parcelable

class DigitalSubscriptionParams(
        var isSubscribed: Boolean = false,
        var showSubscribePopUp: Boolean? = null,
        var autoSubscribe: Boolean? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSubscribed) 1 else 0)
        parcel.writeValue(showSubscribePopUp)
        parcel.writeValue(autoSubscribe)
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