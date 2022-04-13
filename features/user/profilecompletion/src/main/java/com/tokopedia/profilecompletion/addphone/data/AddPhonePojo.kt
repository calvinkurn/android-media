package com.tokopedia.profilecompletion.addphone.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddPhonePojo(
    @SerializedName("userProfileUpdate")
    @Expose
    var data: UserProfileUpdatePhone = UserProfileUpdatePhone()

)

data class UserProfileUpdatePhone(
    @SerializedName("isSuccess")
    @Expose
    var isSuccess: Int = 0,
    @SerializedName("completionScore")
    @Expose
    var completionScore: Int = 0,
    @SerializedName("errors")
    @Expose
    var errors: ArrayList<String> = arrayListOf()
)