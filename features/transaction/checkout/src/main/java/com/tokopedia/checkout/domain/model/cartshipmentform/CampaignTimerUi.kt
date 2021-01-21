package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CampaignTimerUi(
        var dialogButton: String = "",
        var dialogDescription: String = "",
        var dialogTitle: String = "",
        var showTimer: Boolean = false,
        var timerDeduct: String = "",
        var timerDescription: String = "",
        var timerExpired: String = "",
        var timerExpiredDuration: Int = 0,
        var timerServer: String = "",
        var gtmProductId: Long = 0,
        var gtmUserId: String = ""
) : Parcelable