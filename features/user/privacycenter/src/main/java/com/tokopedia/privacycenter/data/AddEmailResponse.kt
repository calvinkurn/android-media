package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class AddEmailResponse(
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
