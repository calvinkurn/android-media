package com.tokopedia.profilecompletion.changename.domain.pojo

data class ChangeNameResult(
    var data: ChangeNamePojo.UserProfileUpdate = ChangeNamePojo.UserProfileUpdate(),
    var fullName: String = ""
)