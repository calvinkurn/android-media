package com.tokopedia.settingbank.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class KYCCheckResponse(
        @SerializedName("kycInfo")
        @Expose
        val kycInfo : KYCInfo
)

@Parcelize
data class KYCInfo(
        @SerializedName("Fullname")
        @Expose
        val fullName : String,
        @SerializedName("IsVerified")
        @Expose
        val isVerified : Boolean,
        @SerializedName("IsExist")
        @Expose
        val isExist : Boolean
) : Parcelable
