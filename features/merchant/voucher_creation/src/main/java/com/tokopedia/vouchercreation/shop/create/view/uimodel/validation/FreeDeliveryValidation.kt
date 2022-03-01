package com.tokopedia.vouchercreation.shop.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

class FreeDeliveryValidation(
        @SerializedName("benefit_idr")
        val benefitIdrError: String = "") : VoucherTypeValidation() {

        fun getIsHaveError() =
                benefitIdrError.isNotBlank() || getIsVoucherError()

}