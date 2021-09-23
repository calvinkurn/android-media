package com.tokopedia.managepassword.haspassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author rival
 * @team @minion-kevin
 */

data class ProfileDataModel(
        @Expose
        @SerializedName("user")
        var profileData: Profile = Profile()
) {
    data class Profile(
            @Expose
            @SerializedName("createdPassword")
            var isCreatedPassword: Boolean = false
    )
}