package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class MerchantVoucherData (
        @SerializedName("is_mvc")
        val isMvc: Int,
        @SerializedName("mvc_title")
        val mvcTitle: String,
        @SerializedName("mvc_logo")
        val mvcLogo: String,
        @SerializedName("mvc_error_message")
        val mvcErrorMessage: String
)