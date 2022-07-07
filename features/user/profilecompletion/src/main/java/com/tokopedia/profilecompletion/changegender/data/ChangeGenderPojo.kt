package com.tokopedia.profilecompletion.changegender.data

import com.google.gson.annotations.SerializedName

data class ChangeGenderPojo(
    @SerializedName("userProfileCompletionUpdate")
    var data: UserProfileCompletionGenderUpdate = UserProfileCompletionGenderUpdate()

)

data class UserProfileCompletionGenderUpdate(
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false,
    @SerializedName("genderMessage")
    var errorMessage: String = "",
    @SerializedName("completionScore")
    var completionScore: Int = 0
)