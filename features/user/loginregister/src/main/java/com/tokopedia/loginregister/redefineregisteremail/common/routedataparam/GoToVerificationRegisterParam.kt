@file:SuppressLint("ParamFieldAnnotation")
package com.tokopedia.loginregister.redefineregisteremail.common.routedataparam

import android.annotation.SuppressLint

data class GoToVerificationRegisterParam(
    val phone: String = "",
    val email: String = "",
    val otpType: Int = 0,
    val otpMode: String = "",
    val source: String = "",
    val token: String = ""
)
