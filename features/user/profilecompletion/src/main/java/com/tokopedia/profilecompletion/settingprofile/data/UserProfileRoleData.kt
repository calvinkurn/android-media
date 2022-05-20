package com.tokopedia.profilecompletion.settingprofile.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-08-01.
 * ade.hadian@tokopedia.com
 */

data class UserProfileRoleData(
    @SerializedName("userProfileRole")
    @Expose
    var profileRoleData: ProfileRoleData = ProfileRoleData()
)