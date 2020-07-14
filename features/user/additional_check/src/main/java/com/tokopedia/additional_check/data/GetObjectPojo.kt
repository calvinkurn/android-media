package com.tokopedia.additional_check.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

data class GetObjectPojo(
        @SerializedName("status")
        val status: Boolean = false,

        @SerializedName("data")
        val twoFactorResult: TwoFactorResult? = null
)

@Parcelize
data class TwoFactorResult(
        @SerializedName("interval")
        val interval: Long = 0L,
        @SerializedName("isSkipable")
        val isSkipable: Boolean = false,
        @SerializedName("isHavePin")
        val isHavePin: Boolean = false,
        @SerializedName("isHavePhone")
        val isHavePhone: Boolean = false
): Parcelable