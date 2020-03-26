package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChangeSection

object ChangeItemDataView {

    fun changeEmail(email: String) = ChangeSection(
            description = R.string.settingnotif_change_email,
            changeItem = email
    )

}