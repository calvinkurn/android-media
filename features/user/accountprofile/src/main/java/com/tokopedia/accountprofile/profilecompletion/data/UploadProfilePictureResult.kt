package com.tokopedia.accountprofile.profilecompletion.data

import com.tokopedia.accountprofile.data.UploadProfileImageModel

data class UploadProfilePictureResult(
    var uploadProfileImageModel: UploadProfileImageModel = UploadProfileImageModel(),
    var submitProfilePictureData: SubmitProfilePictureData = SubmitProfilePictureData()
)
