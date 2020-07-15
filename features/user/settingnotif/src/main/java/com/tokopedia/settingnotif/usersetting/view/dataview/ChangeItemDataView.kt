package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.ChangeSection
import com.tokopedia.settingnotif.usersetting.state.Email
import com.tokopedia.settingnotif.usersetting.state.Phone

object ChangeItemDataView {

    fun changeEmail(email: String) = ChangeSection(
            icon = R.drawable.ic_notifsetting_change_email,
            description = R.string.settingnotif_change_email,
            changeItem = email,
            state = Email
    )

    fun changePhoneNumber(phoneNumber: String) = ChangeSection(
            icon = R.drawable.ic_notifsetting_change_phone,
            description = R.string.settingnotif_change_phone_number,
            changeItem = phoneNumber,
            state = Phone
    )

}