package com.tokopedia.accountprofile.settingprofile.addemail.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CheckEmailPojo(
    @SerializedName("userProfileCompletionValidate")
    var data: UserProfileCompletionValidate = UserProfileCompletionValidate()

)

data class UserProfileCompletionValidate(
    @SuppressLint("Invalid Data Type")
    @SerializedName("isValid")
    var isValid: Boolean = false,
    @SerializedName("emailMessage")
    var errorMessage: String = ""
)
