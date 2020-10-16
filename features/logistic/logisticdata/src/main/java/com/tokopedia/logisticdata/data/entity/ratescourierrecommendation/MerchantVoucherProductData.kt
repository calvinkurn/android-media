package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class MerchantVoucherProductData(
        @SerializedName("is_mvc")
        val isMvc: Boolean,
        @SerializedName("mvc_logo")
        val mvcLogo: String,
        @SerializedName("mvc_error_message")
        val mvcErrorMessage: String
)