package com.tokopedia.profilecommon.domain.param

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class UpdateUserProfileParam @JvmOverloads constructor(
        val fullname: String = "",
        val gender: String = "",
        val birthdate: String = "",
        val phone: String = "",
        val email: String = ""
) {

    fun toMap(): Map<String, Any> = mapOf(
            PARAM_FULLNAME to fullname,
            PARAM_GENDER to gender,
            PARAM_BIRTHDATE to birthdate,
            PARAM_PHONE to phone,
            PARAM_EMAIL to email
    )

    companion object {
        private const val PARAM_FULLNAME = "fullname"
        private const val PARAM_GENDER = "gender"
        private const val PARAM_BIRTHDATE = "birthdate"
        private const val PARAM_PHONE = "phone"
        private const val PARAM_EMAIL = "email"
    }
}