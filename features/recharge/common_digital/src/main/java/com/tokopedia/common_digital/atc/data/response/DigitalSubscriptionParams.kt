package com.tokopedia.common_digital.atc.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DigitalSubscriptionParams(
        var isSubscribed: Boolean = false,
        var showSubscribePopUp: Boolean? = null,
        var autoSubscribe: Boolean? = null
) : Parcelable {
    companion object {
        const val ARG_SHOW_SUBSCRIBE_POP_UP = "show_subscribe_pop_up"
        const val ARG_AUTO_SUBSCRIBE = "auto_subscribe"
    }
}