package com.tokopedia.profilecompletion.settingprofile.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-08-01.
 * ade.hadian@tokopedia.com
 */

data class ProfileRoleData(
    @SerializedName("isAllowedChangeDob")
    @Expose
    var isAllowedChangeDob: Boolean = false,
    @SerializedName("isAllowedChangeName")
    @Expose
    var isAllowedChangeName: Boolean = false,
    @SerializedName("isAllowedChangeGender")
    @Expose
    var isAllowedChangeGender: Boolean = false,
    @SerializedName("chancesChangeName")
    @Expose
    var chancesChangeName: Int = 0,
    @SerializedName("chancesChangeGender")
    @Expose
    var chancesChangeGender: Int = 0
)