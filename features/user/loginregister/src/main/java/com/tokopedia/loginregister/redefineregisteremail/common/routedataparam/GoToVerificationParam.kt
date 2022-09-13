package com.tokopedia.loginregister.redefineregisteremail.common.routedataparam

import android.annotation.SuppressLint

data class GoToVerificationParam(

    @SuppressLint("ParamFieldAnnotation")
    val phone: String = "",

    @SuppressLint("ParamFieldAnnotation")
    val email: String = "",

    @SuppressLint("ParamFieldAnnotation")
    val otpType: Int,

    @SuppressLint("ParamFieldAnnotation")
    val source: String = "",

    @SuppressLint("ParamFieldAnnotation")
    val token: String = ""

)
