package com.tokopedia.profilecompletion.addemail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddEmailPojo(
        @SerializedName("userProfileCompletionUpdate")
        @Expose
        var data: UserProfileCompletionUpdateEmail = UserProfileCompletionUpdateEmail()

)

data class UserProfileCompletionUpdateEmail(
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("emailMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("completionScore")
        @Expose
        var completionScore: Int = 0
)