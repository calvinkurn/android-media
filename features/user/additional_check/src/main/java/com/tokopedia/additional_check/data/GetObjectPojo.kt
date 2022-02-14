package com.tokopedia.additional_check.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

data class GetObjectPojo(
        @SerializedName("show_interrupt")
        val twoFactorResult: TwoFactorResult = TwoFactorResult()
)

@Parcelize
data class TwoFactorResult(
        @SerializedName("interval")
        var interval: Int = 0,
        @SerializedName("show_skip")
        var showSkipButton: Boolean = false,
        @SerializedName("popup_2fa")
        var popupType: Int = 0,
        @SerializedName("error")
        var error: String = ""

): Parcelable