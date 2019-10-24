package com.tokopedia.profilecompletion.changename.data

data class ChangeNameResult(
        var data: ChangeNamePojo.UserProfileUpdate = ChangeNamePojo.UserProfileUpdate(),
        var fullName: String = ""
)