package com.tokopedia.vouchercreation.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

data class VoucherTargetValidation (
        @SerializedName("is_public")
        val isPublicError: String = "",
        @SerializedName("code")
        val codeError: String = "",
        @SerializedName("coupon_type")
        val couponNameError: String = ""
)