package com.tokopedia.profilecompletion.profileinfo.data

import com.google.gson.annotations.SerializedName

data class ProfileRoleResponse(
    @SerializedName("userProfileRole")
    var profileRole: ProfileRoleData = ProfileRoleData()
)

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
    var chancesChangeGender: String = "0",
    @SerializedName("changeNameMessageInfoTitle")
    var changeNameMessageInfoTitle: String = "",
    @SerializedName("changeNameMessageInfo")
    var changeNameMessageInfo: String = "",
    @SerializedName("changeDobMessageInfoTitle")
    var changeDobMessageInfoTitle: String = "",
    @SerializedName("changeDobMessageInfo")
    var changeDobMessageInfo: String = ""

)