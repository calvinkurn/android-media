package com.tokopedia.tokomember_seller_dashboard.model

import android.annotation.SuppressLint

@SuppressLint("ResponseFieldAnnotation")
data class TmCouponValidationResponse(
    var uploadUrlPremium: String = "",
    var uploadIdPremium: String = "",
    var uploadIdVip: String = "",
    var uploadUrlVip: String = "",
    var tmVoucherValidationPartialResponseVip: TmVoucherValidationPartialResponse = TmVoucherValidationPartialResponse(),
    var tmVoucherValidationPartialResponsePremium: TmVoucherValidationPartialResponse = TmVoucherValidationPartialResponse(),
    var memberShipValidateResponse: MemberShipValidateResponse = MemberShipValidateResponse()
)