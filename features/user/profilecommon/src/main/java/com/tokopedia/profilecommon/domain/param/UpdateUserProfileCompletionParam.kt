package com.tokopedia.profilecommon.domain.param

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class UpdateUserProfileCompletionParam @JvmOverloads constructor(
        val fullname: String = "",
        val gender: Int = 0,
        val birthdate: String = "",
        val password: String = "",
        val passwordConfirm: String = "",
        val email: String = "",
        val msisdn: String = "",
        val skipOTP: Boolean = false,
        val otpCode: String = ""
) {

    fun toMap(): Map<String, Any> = mapOf(
            PARAM_FULLNAME to fullname,
            PARAM_GENDER to gender,
            PARAM_BIRTHDATE to birthdate,
            PARAM_PASSWORD to password,
            PARAM_PASSWORD_CONFIRM to passwordConfirm,
            PARAM_EMAIL to email,
            PARAM_MSISDN to msisdn,
            PARAM_SKIP_OTP to skipOTP,
            PARAM_OTP_CODE to otpCode
    )

    companion object {
        private const val PARAM_FULLNAME = "fullname"
        private const val PARAM_GENDER = "gender"
        private const val PARAM_BIRTHDATE = "birthdate"
        private const val PARAM_PASSWORD = "password"
        private const val PARAM_PASSWORD_CONFIRM = "passwordConfirm"
        private const val PARAM_EMAIL = "email"
        private const val PARAM_MSISDN = "msisdn"
        private const val PARAM_SKIP_OTP = "skipOTP"
        private const val PARAM_OTP_CODE = "otpCode"
    }
}