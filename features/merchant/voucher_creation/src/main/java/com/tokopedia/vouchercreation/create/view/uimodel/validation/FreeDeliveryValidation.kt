package com.tokopedia.vouchercreation.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

data class FreeDeliveryValidation(
        @SerializedName("benefit_idr")
        val benefitIdrError: String = "") : VoucherTypeValidation()