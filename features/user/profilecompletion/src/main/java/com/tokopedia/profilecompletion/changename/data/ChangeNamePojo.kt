package com.tokopedia.profilecompletion.changename.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangeNamePojo(
        @SerializedName("userProfileUpdate")
        @Expose
        var data: UserProfileUpdate = UserProfileUpdate()
) {
    data class UserProfileUpdate(
            @SerializedName("isSuccess")
            @Expose
            var isSuccess: Boolean = false,
            @SerializedName("completionScore")
            @Expose
            var completionScore: Int = 0,
            @SerializedName("errors")
            @Expose
            var errors: String = ""
    )
}