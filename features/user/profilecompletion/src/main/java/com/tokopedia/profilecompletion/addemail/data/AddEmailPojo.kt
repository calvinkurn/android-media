package com.tokopedia.profilecompletion.addemail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddEmailPojo(
        @SerializedName("data")
        @Expose
        var data: AddEmailData = AddEmailData()
)

data class AddEmailData(
        @SerializedName("userProfileCompletionUpdate")
        @Expose
        var userProfileCompletionUpdate: UserProfileCompletionUpdateEmail = UserProfileCompletionUpdateEmail()
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