package com.tokopedia.logintest.common

import android.util.Patterns
import com.tokopedia.kotlin.extensions.view.toLongOrString

/**
 * Created by Ade Fulki on 2019-07-26.
 * ade.hadian@tokopedia.com
 */

class PartialRegisterInputUtils{
    companion object {

        const val UNDEFINE_TYPE: Int = 0
        const val PHONE_TYPE: Int = 1
        const val EMAIL_TYPE: Int = 2

        @JvmStatic
        fun isValidEmail(email: String): Boolean = (!email.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches())

        @JvmStatic
        fun isValidPhone(phone: String): Boolean = Patterns.PHONE.matcher(phone).matches() &&
                phone.length >= 6

        @JvmStatic
        fun getType(value: String): Int {
            return when(value.toLongOrString()){
                is Long -> PHONE_TYPE
                is String -> EMAIL_TYPE
                else -> UNDEFINE_TYPE
            }
        }
    }
}