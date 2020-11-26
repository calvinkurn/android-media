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
        val twoFactorResult: TwoFactorResult? = null
)

@Parcelize
data class TwoFactorResult(
        @SerializedName("interval")
        val interval: Int = 0,
        @SerializedName("show_skip")
        val showSkipButton: Boolean = false,
        @SerializedName("popup_2fa")
        val popupType: Int = 0,
        @SerializedName("error")
        val error: String = ""

): Parcelable