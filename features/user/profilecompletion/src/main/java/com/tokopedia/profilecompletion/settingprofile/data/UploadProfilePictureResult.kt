package com.tokopedia.profilecompletion.settingprofile.data

import com.tokopedia.profilecompletion.data.UploadProfileImageModel

data class UploadProfilePictureResult(
        var uploadProfileImageModel: UploadProfileImageModel = UploadProfileImageModel(),
        var submitProfilePictureData: SubmitProfilePictureData = SubmitProfilePictureData()
)