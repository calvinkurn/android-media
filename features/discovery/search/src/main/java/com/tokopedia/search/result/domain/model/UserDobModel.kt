package com.tokopedia.search.result.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfileDobModel(
    @SerializedName("userProfileDob")
    @Expose
    val userDOB: UserDOB = UserDOB(),
)

data class UserDOB(
        @SerializedName("isAdult")
        @Expose
        val isAdult: Boolean = false,
        @SerializedName("isDobVerified")
        @Expose
        val isDobVerified: Boolean = false,
) {

    fun isAdultAndVerify() = isAdult && isDobVerified

}
