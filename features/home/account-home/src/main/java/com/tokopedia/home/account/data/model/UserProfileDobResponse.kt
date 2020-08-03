package com.tokopedia.home.account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfileDobResponse(
        @SerializedName("userProfileDob")
        @Expose
        val userProfileDob: UserProfileDob
)