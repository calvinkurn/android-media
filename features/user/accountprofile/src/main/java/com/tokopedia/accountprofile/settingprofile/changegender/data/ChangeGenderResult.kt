package com.tokopedia.accountprofile.settingprofile.changegender.data

data class ChangeGenderResult(
    var data: UserProfileCompletionGenderUpdate = UserProfileCompletionGenderUpdate(),
    var selectedGender: Int = 1
)
