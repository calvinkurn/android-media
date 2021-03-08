package com.tokopedia.settingbank.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KYCCheckResponse(
        @SerializedName("kycInfo")
        @Expose
        val kycInfo : KYCInfo
)

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
)
