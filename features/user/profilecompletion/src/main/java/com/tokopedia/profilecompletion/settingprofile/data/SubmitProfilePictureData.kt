package com.tokopedia.profilecompletion.settingprofile.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitProfilePictureData(
        @SerializedName("change_picture")
        @Expose
        var changePictureData: ChangePictureData = ChangePictureData()
)

data class ChangePictureData(
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Int = 0,
        @SerializedName("error")
        @Expose
        var error: String = ""
)