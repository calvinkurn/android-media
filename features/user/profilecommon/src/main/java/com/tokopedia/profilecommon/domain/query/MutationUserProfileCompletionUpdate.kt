package com.tokopedia.profilecommon.domain.query

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

object MutationUserProfileCompletionUpdate {

    private const val fullname = "\$fullname"
    private const val gender = "\$gender"
    private const val birthdate = "\$birthdate"
    private const val password = "\$password"
    private const val passwordConfirm = "\$passwordConfirm"
    private const val email = "\$email"
    private const val msisdn = "\$msisdn"
    private const val skipOTP = "\$skipOTP"
    private const val otpCode = "\$otpCode"

    fun getQuery(): String = """
        mutation userProfileCompletionUpdate(
                $fullname: String,
                $gender: Int,
                $birthdate: String,
                $password: String,
                $passwordConfirm: String,
                $email: String,
                $msisdn: String,
                $skipOTP: Boolean,
                $otpCode: String,
        ) {
            userProfileCompletionUpdate(
                    fullname: $fullname,
                    gender: $gender,
                    birthdate: $birthdate,
                    password: $password,
                    passwordConfirm: $passwordConfirm,
                    email: $email,
                    msisdn: $msisdn,
                    skipOTP: $skipOTP,
                    otpCode: $otpCode
            ) {
                isSuccess,
                completionScore,
                completionDone,
                fullNameMessage,
                genderMessage,
                birthDateMessage,
                passwordMessage,
                passwordConfirmMessage,
                emailMessage,
                msisdnMessage
            }
        }
    """.trimIndent()
}