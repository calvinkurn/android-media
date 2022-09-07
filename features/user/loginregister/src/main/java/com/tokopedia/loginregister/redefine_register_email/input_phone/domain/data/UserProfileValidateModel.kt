package com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class UserProfileValidateModel(

    @SerializedName("userProfileValidate")
    var data: UserProfileValidate = UserProfileValidate()

)

data class UserProfileValidate(

    @SuppressLint("Invalid Data Type")
    @SerializedName("isValid")
    var isValid: Boolean = false,

    @SerializedName("message")
    var message: String = ""

)