package com.tokopedia.vouchercreation.shop.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

data class CashbackRupiahValidation (
        @SerializedName("benefit_idr")
        val benefitMaxError: String = ""
) : VoucherTypeValidation(), Validation {

        fun getIsHaveError() =
                benefitMaxError.isNotBlank() || getIsVoucherError()
}