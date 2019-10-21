package com.tokopedia.profilecompletion.changegender.data

data class ChangeGenderResult(
        var data: UserProfileCompletionGenderUpdate = UserProfileCompletionGenderUpdate(),
        var selectedGender: Int = 1
)