package com.tokopedia.loginregister.shopcreation.domain.query

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

object MutationUserProfileUpdate {

    private const val fullname = "\$fullname"
    private const val gender = "\$gender"
    private const val birthdate = "\$birthdate"
    private const val phone = "\$phone"
    private const val email = "\$email"

    fun getQuery(): String = """
        mutation userProfileUpdate(
                $fullname: String,
                $gender: String,
                $birthdate: String,
                $phone: String,
                $email: String
        ) {
            userProfileUpdate(
                    fullname: $fullname,
                    gender: $gender,
                    birthdate: $birthdate,
                    phone: $phone,
                    email: $email
            ) {
                isSuccess,
                completionScore,
                errors
            }
        }
    """.trimIndent()
}