package com.tokopedia.profilecompletion.changegender.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangeGenderPojo(
        @SerializedName("data")
        @Expose
        var changeGenderData: ChangeGenderData = ChangeGenderData()
)

data class ChangeGenderData(
        @SerializedName("userProfileCompletionUpdate")
        @Expose
        var userProfileCompletionUpdate: UserProfileCompletionUpdate = UserProfileCompletionUpdate()
)

data class UserProfileCompletionUpdate(
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