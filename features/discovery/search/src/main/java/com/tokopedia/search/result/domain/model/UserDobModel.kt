package com.tokopedia.search.result.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserProfileDobModel(
    @SerializedName("userProfileDob")
    @Expose
    var userDOB: UserDOB = UserDOB(),
)

data class UserDOB(
        @SerializedName("isAdult")
        @Expose
        var isAdult: Boolean = false,
        @SerializedName("isDobVerified")
        @Expose
        var isDobVerified: Boolean = false,
) {

    fun isAdultAndVerify() = isAdult && isDobVerified

}
