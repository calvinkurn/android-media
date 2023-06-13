package com.tokopedia.profilecompletion.profilecompletion.data

import com.google.gson.annotations.SerializedName

data class SubmitProfilePictureData(
    @SerializedName("change_picture")
    var changePictureData: ChangePictureData = ChangePictureData()
)

data class ChangePictureData(
    @SerializedName("isSuccess")
    var isSuccess: Int = 0,
    @SerializedName("error")
    var error: String = ""
)