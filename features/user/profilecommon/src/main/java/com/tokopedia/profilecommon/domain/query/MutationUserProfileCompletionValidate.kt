package com.tokopedia.profilecommon.domain.query

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

object MutationUserProfileCompletionValidate {

    private const val fullname = "\$fullname"
    private const val gender = "\$gender"
    private const val birthdate = "\$birthdate"
    private const val password = "\$password"
    private const val passwordConfirm = "\$passwordConfirm"
    private const val email = "\$email"
    private const val msisdn = "\$msisdn"

    fun getQuery(): String = """
        mutation userProfileCompletionValidate(
                $fullname: String,
                $gender: Int,
                $birthdate: String,
                $password: String,
                $passwordConfirm: String,
                $email: String,
                $msisdn: String) {
            userProfileCompletionValidate(
                    fullname: $fullname,
                    gender: $gender,
                    birthdate: $birthdate,
                    password: $password,
                    passwordConfirm: $passwordConfirm,
                    email: $email,
                    msisdn: $msisdn) {
                isDirectEdit,
                isValid,
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