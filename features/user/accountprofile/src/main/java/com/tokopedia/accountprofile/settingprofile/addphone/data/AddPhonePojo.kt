package com.tokopedia.accountprofile.settingprofile.addphone.data

import com.google.gson.annotations.SerializedName

data class AddPhonePojo(
    @SerializedName("userProfileUpdate")
    var data: UserProfileUpdatePhone = UserProfileUpdatePhone()

)

data class UserProfileUpdatePhone(
    @SerializedName("isSuccess")
    var isSuccess: Int = 0,
    @SerializedName("completionScore")
    var completionScore: Int = 0,
    @SerializedName("errors")
    var errors: ArrayList<String> = arrayListOf()
)
