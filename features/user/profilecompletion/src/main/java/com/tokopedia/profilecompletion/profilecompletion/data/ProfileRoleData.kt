package com.tokopedia.profilecompletion.profilecompletion.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-08-01.
 * ade.hadian@tokopedia.com
 */

data class ProfileRoleData(
    @SerializedName("isAllowedChangeDob")
    var isAllowedChangeDob: Boolean = false,
    @SerializedName("isAllowedChangeName")
    var isAllowedChangeName: Boolean = false,
    @SerializedName("isAllowedChangeGender")
    var isAllowedChangeGender: Boolean = false,
    @SerializedName("chancesChangeName")
    var chancesChangeName: String = "0",
    @SerializedName("chancesChangeGender")
    var chancesChangeGender: String = "0"
)