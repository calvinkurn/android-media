package com.tokopedia.vouchercreation.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

open class VoucherTypeValidation (
        @SerializedName("benefit_type")
        val benefitTypeError: String = "",
        @SerializedName("coupon_type")
        val couponTypeError: String = "",
        @SerializedName("min_purchase")
        val minPurchaseError: String = "",
        @SerializedName("quota")
        val quotaError: String = ""
) : Validation