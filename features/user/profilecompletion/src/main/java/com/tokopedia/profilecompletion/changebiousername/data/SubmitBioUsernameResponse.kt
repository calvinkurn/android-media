package com.tokopedia.profilecompletion.changebiousername.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitBioUsernameResponse(
    @SerializedName("feedXProfileSubmit")
    val response: SubmitBioUsername = SubmitBioUsername()
)

data class SubmitBioUsername(
    @SerializedName("status")
    val status: Boolean = false
)