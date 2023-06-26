package com.tokopedia.loginregister.shopcreation.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class UserProfileValidatePojo(
        @SerializedName("userProfileValidate") @Expose
        var data: UserProfileValidate = UserProfileValidate()
)

data class UserProfileValidate(
        @SuppressLint("Invalid Data Type")
        @SerializedName("isValid") @Expose
        var isValid: Boolean = false,
        @SerializedName("message") @Expose
        var message: String = ""
)
