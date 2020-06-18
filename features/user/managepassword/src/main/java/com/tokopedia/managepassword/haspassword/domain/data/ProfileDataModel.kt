package com.tokopedia.managepassword.haspassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author rival
 * @team @minion-kevin
 */

data class ProfileDataModel(
        @Expose
        @SerializedName("userProfileCompletion")
        var profileData: Profile = Profile()
) {
    data class Profile(
            @Expose
            @SerializedName("isCreatedPassword")
            var isCreatedPassword: Boolean = false
    )
}