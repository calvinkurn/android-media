package com.tokopedia.loginregister.shopcreation.domain.query

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

object MutationUserProfileValidate {

    private const val phone = "\$phone"
    private const val email = "\$email"
    private const val validateToken = "\$validateToken"

    fun getQuery(): String = """
        mutation userProfileValidate($phone: String, $email: String, $validateToken: String) {
            userProfileValidate(phone: $phone, email: $email, validateToken: $validateToken) {
                isValid,
                message
            }
        }
    """.trimIndent()
}