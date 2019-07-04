package com.tokopedia.profilecompletion.addphone.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddPhonePojo(
        @SerializedName("data")
        @Expose
        var data: AddPhoneData = AddPhoneData()
)

data class AddPhoneData(
        @SerializedName("userProfileCompletionUpdate")
        @Expose
        var userProfileCompletionUpdate: UserProfileCompletionUpdatePhone = UserProfileCompletionUpdatePhone()
)

data class UserProfileCompletionUpdatePhone(
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("msisdnMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("completionScore")
        @Expose
        var completionScore: Int = 0
)