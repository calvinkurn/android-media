package com.tokopedia.accountprofile.settingprofile.changename.domain.pojo

data class ChangeNameResult(
    var data: ChangeNamePojo.UserProfileUpdate = ChangeNamePojo.UserProfileUpdate(),
    var fullName: String = ""
)
