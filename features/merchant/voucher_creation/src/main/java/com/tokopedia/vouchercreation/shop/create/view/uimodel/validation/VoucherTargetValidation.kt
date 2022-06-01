package com.tokopedia.vouchercreation.shop.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

data class VoucherTargetValidation (
        @SerializedName("is_public")
        val isPublicError: String = "",
        @SerializedName("code")
        val codeError: String = "",
        @SerializedName("coupon_name")
        val couponNameError: String = "") : Validation {

        fun checkHasError() =
                !(isPublicError.isBlank() && codeError.isBlank() && couponNameError.isBlank())

}