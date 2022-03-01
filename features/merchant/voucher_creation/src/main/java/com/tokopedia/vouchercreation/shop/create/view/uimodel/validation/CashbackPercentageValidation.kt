package com.tokopedia.vouchercreation.shop.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

data class CashbackPercentageValidation (
        @SerializedName("benefit_percent")
        val benefitPercentError: String = "",
        @SerializedName("benefit_max")
        val benefitMaxError: String = ""
) : VoucherTypeValidation(), Validation {

        fun getIsHaveError() =
                benefitPercentError.isNotBlank() || benefitMaxError.isNotBlank() || getIsVoucherError()
}