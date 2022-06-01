package com.tokopedia.tokomember_seller_dashboard.util

sealed class CouponErrorType

data class UploadVipCouponError(var errorMessage: String = "Upload Failed Coupon VIP") :
    CouponErrorType()
data class UploadPremiumCouponError(var errorMessage: String = "Upload Failed Coupon Premium") :
    CouponErrorType()
data class ValidateCouponError(var errorMessage: String = "Membership Validation Error") :
    CouponErrorType()
data class ValidateVipError(var errorMessage: String = "Please Validate Vip Coupon") :
    CouponErrorType()
data class ValidatePremiumError(var errorMessage: String = "Please Validate Premium Coupon") :
    CouponErrorType()

data class ErrorState(
    var isValidateCouponError: Boolean = false,
    var isPreValidateVipError: Boolean = false,
    var isPreValidatePremiumError: Boolean = false,
    var isUploadVipError: Boolean = false,
    var isUploadPremium: Boolean = false,
)