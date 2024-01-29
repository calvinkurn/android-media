package com.tokopedia.profilecompletion.settingprofile.addemail.data

import com.google.gson.annotations.SerializedName

data class AddEmailPojo(
    @SerializedName("userProfileCompletionUpdate")
    var data: UserProfileCompletionUpdateEmail = UserProfileCompletionUpdateEmail()

)

data class UserProfileCompletionUpdateEmail(
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false,
    @SerializedName("emailMessage")
    var errorMessage: String = "",
    @SerializedName("completionScore")
    var completionScore: Int = 0
)
