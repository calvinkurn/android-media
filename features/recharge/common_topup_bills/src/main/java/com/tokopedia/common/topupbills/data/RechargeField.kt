package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RechargeField (
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
): Parcelable
