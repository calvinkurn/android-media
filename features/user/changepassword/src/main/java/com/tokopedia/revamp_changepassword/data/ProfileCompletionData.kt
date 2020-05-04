package com.tokopedia.revamp_changepassword.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author rival
 * @team @minion-kevin
 */

data class ProfileCompletionData(
        @Expose
        @SerializedName("userProfileCompletion")
        var userProfileData: UserProfileCompletion = UserProfileCompletion()
) {
    data class UserProfileCompletion(
            @Expose
            @SerializedName("isCreatedPassword")
            var isCreatedPassword: Boolean = false
    )
}