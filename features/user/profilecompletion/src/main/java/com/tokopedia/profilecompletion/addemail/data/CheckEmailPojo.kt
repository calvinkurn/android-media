package com.tokopedia.profilecompletion.addemail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckEmailPojo(
    @SerializedName("userProfileCompletionValidate")
    @Expose
    var data: UserProfileCompletionValidate = UserProfileCompletionValidate()

)

data class UserProfileCompletionValidate(
    @SerializedName("isValid")
    @Expose
    var isValid: Boolean = false,
    @SerializedName("emailMessage")
    @Expose
    var errorMessage: String = ""
)