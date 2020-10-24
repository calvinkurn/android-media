package com.tokopedia.phoneverification.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhoneVerificationResponseData (
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Boolean = false,

        @SerializedName("message")
        @Expose
        var message: String = ""
): Parcelable