package com.tokopedia.digital_checkout.data.response.atc

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DigitalSubscriptionParams(
        var isSubscribed: Boolean = false,
        var showSubscribePopUp: Boolean? = null,
        var autoSubscribe: Boolean? = null
) : Parcelable