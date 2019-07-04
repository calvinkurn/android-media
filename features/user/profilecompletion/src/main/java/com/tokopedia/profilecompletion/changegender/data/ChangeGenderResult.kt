package com.tokopedia.profilecompletion.changegender.data

data class ChangeGenderResult(
        var changeGenderData: ChangeGenderData = ChangeGenderData(),
        var selectedGender: Int = 1
)