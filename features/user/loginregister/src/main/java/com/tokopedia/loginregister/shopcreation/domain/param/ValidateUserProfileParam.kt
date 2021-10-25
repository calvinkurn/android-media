package com.tokopedia.loginregister.shopcreation.domain.param

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class ValidateUserProfileParam @JvmOverloads constructor(
        val phone: String = "",
        val email: String = ""
) {

    fun toMap(): Map<String, Any> = mapOf(
            PARAM_PHONE to phone,
            PARAM_EMAIL to email
    )

    companion object {
        private const val PARAM_PHONE = "phone"
        private const val PARAM_EMAIL = "email"
    }
}