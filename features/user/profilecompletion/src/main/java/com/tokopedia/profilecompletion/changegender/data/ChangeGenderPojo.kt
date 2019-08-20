package com.tokopedia.profilecompletion.changegender.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangeGenderPojo(
        @SerializedName("userProfileCompletionUpdate")
        @Expose
        var data: UserProfileCompletionGenderUpdate = UserProfileCompletionGenderUpdate()

)

data class UserProfileCompletionGenderUpdate(
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("genderMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("completionScore")
        @Expose
        var completionScore: Int = 0
)