package com.tokopedia.profilecompletion.settingprofile.changebiousername.data

import com.google.gson.annotations.SerializedName

data class SubmitBioUsernameResponse(
    @SerializedName("feedXProfileSubmit")
    val response: SubmitBioUsername = SubmitBioUsername()
)

data class SubmitBioUsername(
    @SerializedName("status")
    val status: Boolean = false
)
