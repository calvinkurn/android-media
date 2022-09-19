package com.tokopedia.profilecompletion.profilecompletion.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-08-01.
 * ade.hadian@tokopedia.com
 */

data class UserProfileRoleData(
    @SerializedName("userProfileRole")
    var profileRoleData: ProfileRoleData = ProfileRoleData()
)