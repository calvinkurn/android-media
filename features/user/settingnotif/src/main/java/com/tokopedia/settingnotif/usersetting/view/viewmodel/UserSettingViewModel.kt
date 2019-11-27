package com.tokopedia.settingnotif.usersetting.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory

class UserSettingViewModel(
        var data: List<Visitable<SettingFieldTypeFactory>>
) {

}