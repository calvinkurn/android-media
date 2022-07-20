package com.tokopedia.vouchercreation.shop.create.domain.model.validation

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class VoucherValidationPartialResponse(
        @SerializedName("VoucherValidationPartial")
        val response: VoucherValidationPartialData = VoucherValidationPartialData()
)

data class VoucherValidationPartialData(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val validationPartialErrorData: VoucherValidationPartialError = VoucherValidationPartialError()
)

data class VoucherValidationPartialError(
        @SerializedName("validation_error")
        val validationPartial: VoucherValidationPartial = VoucherValidationPartial()
)

data class VoucherValidationPartial(
        @SerializedName("benefit_idr")
        val benefitError: String = "",
        @SerializedName("benefit_max")
        val benefitMaxError: String = "",
        @SerializedName("benefit_percent")
        val benefitPercentError: String = "",
        @SerializedName("benefit_type")
        val benefitTypeError: String = "",
        @SerializedName("code")
        val promoCodeError: String = "",
        @SerializedName("coupon_name")
        val couponNameError: String = "",
        @SerializedName("coupon_type")
        val couponTypeError: String = "",
        @SerializedName("date_end")
        val dateEndError: String = "",
        @SerializedName("date_start")
        val dateStartError: String = "",
        @SerializedName("hour_end")
        val hourEndError: String = "",
        @SerializedName("hour_start")
        val hourStartError: String ="",
        @SerializedName("image")
        val imageError: String = "",
        @SerializedName("image_square")
        val imageSquareError: String = "",
        @SerializedName("is_public")
        val isPublicError: String = "",
        @SerializedName("min_purchase")
        val minPurchaseError: String = "",
        @SerializedName("quota")
        val quotaError: String = ""
)