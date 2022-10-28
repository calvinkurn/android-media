package com.tokopedia.profilecompletion.addphone.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-08-05.
 * ade.hadian@tokopedia.com
 */

data class UserValidatePojo(
    @SerializedName("userProfileValidate")
    var userProfileValidate: UserProfileValidate = UserProfileValidate()
)

data class UserProfileValidate(
    @SuppressLint("Invalid Data Type")
    @SerializedName("isValid")
    var isValid: Boolean = false,
    @SerializedName("message")
    var message: String = ""
)