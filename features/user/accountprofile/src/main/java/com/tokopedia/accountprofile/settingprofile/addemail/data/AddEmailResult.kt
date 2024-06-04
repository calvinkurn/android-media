package com.tokopedia.accountprofile.settingprofile.addemail.data

data class AddEmailResult(
    val addEmailPojo: AddEmailPojo = AddEmailPojo(),
    val email: String = ""
)
