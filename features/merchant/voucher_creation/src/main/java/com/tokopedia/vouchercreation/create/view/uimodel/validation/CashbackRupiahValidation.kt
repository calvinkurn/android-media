package com.tokopedia.vouchercreation.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

data class CashbackRupiahValidation (
        @SerializedName("benefit_max")
        val benefitMaxError: String = ""
) : VoucherTypeValidation(), Validation {

        fun getIsHaveError() =
                benefitMaxError.isNotBlank() || getIsVoucherError()
}